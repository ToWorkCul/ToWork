package com.culproject.towork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class SeekerHomeActivity extends AppCompatActivity {

    private TextView title;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth firebaseAuth;
    private FusedLocationProviderClient client;
    private Location currentLocation;
    ArrayList<Request> requestList = new ArrayList<Request>();

    public View.OnClickListener createRequestListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), CreateRequestActivity.class);
            startActivity(intent);
            return;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_home);

        firebaseAuth = FirebaseAuth.getInstance();

        client = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        tabLayout = (TabLayout) findViewById(R.id.serviceTabLayout);
        viewPager = (ViewPager) findViewById(R.id.serviceViewPager);
        title = (TextView) findViewById(R.id.seekerTitleTextView);

        tabLayout.setupWithViewPager(viewPager);
        setViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences preferences = getSharedPreferences("UserSetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        finish();
    }

    private void requestPermission(){

        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }

    }

    public void getCurrentLocation() {
        requestPermission();

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        };

        client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.w("TAG", "Exitoso:1234: .----" + location.getLongitude());
                    SharedPreferences preferences = getSharedPreferences("CurrentLocations", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putFloat("latitude", (float) location.getLatitude());
                    editor.putFloat("longitude", (float) location.getLatitude());
                    Log.w("TAG", "lat: " + location.getLatitude());
                    editor.commit();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.createRequestMenuItem:
                Intent intent = new Intent(getBaseContext(), CreateRequestActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setViewPager(ViewPager viewPager) {
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.addFragment(new tabServicesMap(), "Mapa de Servicios");
        tabViewPagerAdapter.addFragment(new tabServicesList(), "Lista de Servicios");

        viewPager.setAdapter(tabViewPagerAdapter);
    }

    private void saveAmountOfrequests() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("requests");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                requestList.clear();
                for (DataSnapshot childDataSnapshot : data.getChildren()) {
                    requestList.add(new Request());
                }
                saveAmountrequest();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "Cancelado", databaseError.toException());
            }
        };
        myRef.addValueEventListener(postListener);
    }


    private void saveAmountrequest() {
        SharedPreferences preferences = getSharedPreferences("UserSetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int size = requestList.size();
        editor.putInt("requestAmount", size);
        editor.commit();
    }

}
