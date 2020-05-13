package com.culproject.towork;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class tabServicesMap extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mapView;
    View mView;



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
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType((googleMap.MAP_TYPE_NORMAL));

        SharedPreferences preferences = getActivity().getSharedPreferences("CurrentLocations", Context.MODE_PRIVATE);
        Float latitude = preferences.getFloat("latitude", (float) 0.0);
        Float longitude = preferences.getFloat("longitude", (float) 0.0);
        Log.w("TAG", "lat: " + latitude);

        //googleMap.addMarker(new MarkerOptions().position(new LatLng(10.790700, -74.763958)).title("statue of Liberty").snippet("Description"));
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(10.790557, -74.763958)).title("statue of Liberty").snippet("Description"));
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("statue of Liberty").snippet("Description"));
        CameraPosition currentLocation = CameraPosition.builder().target(new LatLng(10.790557, -74.763958)).zoom(17).bearing(0).tilt(45).build();
        if (latitude != 0.0 && longitude != 0.0) {
            //googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("statue of Liberty").snippet("Description"));
            //currentLocation = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(16).bearing(0).tilt(45).build();
            Log.w("TAG", "lat: " + latitude);
        }

        googleMap.addMarker(new MarkerOptions().position(new LatLng(10.790557, -74.763958)).title("Casa de Edwin").snippet("Mi casa"));


        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentLocation));
    }

}
