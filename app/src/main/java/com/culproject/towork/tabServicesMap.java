package com.culproject.towork;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.ArrayList;

public class tabServicesMap extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mapView;
    View mView;

    ListView serviceListView;
    Service[] services = {};
    ArrayList<Service> serviceList = new ArrayList<Service>();

    public ValueEventListener postListener;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_services_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) mView.findViewById(R.id.servicesMapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("services");
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                serviceList.clear();
                Log.w("TAG", "Cargando..." + data.toString());
                Log.w("TAG", "Cargando..." +  data.getChildrenCount());


                for (DataSnapshot childDataSnapshot : data.getChildren()) {
                    Log.w("TAG", "Cargando2..." + childDataSnapshot.toString());
                    Log.w("TAG", "Cargando.2.." +  childDataSnapshot.getChildrenCount());
                    if (childDataSnapshot.getChildrenCount() == 6) {
                        try {
                            serviceList.add(new Service(childDataSnapshot));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                loadMarkers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "Cancelado", databaseError.toException());
                // TODO: Manejar escenario cuando falle
            }
        };
        myRef.addValueEventListener(postListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType((googleMap.MAP_TYPE_NORMAL));


        SharedPreferences preferences = getActivity().getSharedPreferences("CurrentLocations", Context.MODE_PRIVATE);
        Float latitude = preferences.getFloat("latitude", (float) 0.0);
        Float longitude = preferences.getFloat("longitude", (float) 0.0);
        Log.w("TAG", "lat: " + latitude);

        CameraPosition currentLocation = CameraPosition.builder().target(new LatLng(10.790557, -74.763958)).zoom(17).bearing(0).tilt(45).build();
        if (latitude != 0.0 && longitude != 0.0) {
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Ubicacion Actual").snippet("Tu Ubicacion"));
            currentLocation = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(17).bearing(0).tilt(45).build();
            Log.w("TAG", "lat: " + latitude + " lon; "+ longitude);
        }


        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentLocation));
    }

    private void loadMarkers() {
        for (Service service: serviceList) {
            Float lat = Float.valueOf(service.getLat());
            Float lon = Float.valueOf(service.getLon());
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title(service.getName())
                    .snippet(service.getDescription())
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.mipmap.logo_hands))));
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
