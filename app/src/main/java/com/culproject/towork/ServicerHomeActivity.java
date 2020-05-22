package com.culproject.towork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.EventListener;
import java.util.Map;

public class ServicerHomeActivity extends AppCompatActivity {

    private TextView title;
    private TextView indicator;
    private Button btnCreateService;
    private Button btnLookRequests;
    private FirebaseAuth firebaseAuth;
    private Map<String, Object> usersData;
    private ProgressDialog progressDialog;

    Request[] requests = {};
    ArrayList<Request> requestList = new ArrayList<Request>();
    CustomAdapter customAdapter = new CustomAdapter();
    ListView requestListView;

    ValueEventListener postListener;

    private FusedLocationProviderClient client;

    public View.OnClickListener createServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), CreateServiceActivity.class);
            startActivity(intent);
            return;
        }
    };

    public View.OnClickListener lookRequestsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), RequestListActivity.class);
            startActivity(intent);
            return;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicer_home);

        client = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        title = (TextView) findViewById(R.id.servicerTitleTextView);

        firebaseAuth = FirebaseAuth.getInstance();

        btnCreateService = (Button) findViewById(R.id.CreateServicesButton);
        btnCreateService.setOnClickListener(createServiceListener);

        btnLookRequests = (Button) findViewById(R.id.LookRequestsButton);
        btnLookRequests.setOnClickListener(lookRequestsListener);


        progressDialog = new ProgressDialog(this);

        requestListView = (ListView) findViewById(R.id.servicerRequestListView);
        requestListView.setAdapter(customAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("requests");

        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {

                Log.w("TAG", "Cargando..." + data.getChildren().toString());
                requestList.clear();
                for (DataSnapshot childDataSnapshot : data.getChildren()) {
                    progressDialog.dismiss();
                    if (childDataSnapshot.getChildrenCount() == 6) {
                        try {
                            if (shouldAddRequest(childDataSnapshot.child("servicer_id").getValue().toString())) {
                                requestList.add(new Request(childDataSnapshot));
                                requestListView.setAdapter(customAdapter);
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }
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
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences preferences = getSharedPreferences("UserSetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.service_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.createServiceMenuItem:
                Intent intent = new Intent(getBaseContext(), CreateServiceActivity.class);
                startActivity(intent);
            case  R.id.actualizarListaSolicitudes:
                updateRequests();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateRequests() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("requests");

        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {

                Log.w("TAG", "Cargando..." + data.getChildren().toString());
                requestList.clear();
                for (DataSnapshot childDataSnapshot : data.getChildren()) {
                    progressDialog.dismiss();
                    if (childDataSnapshot.getChildrenCount() == 6) {
                        try {
                            if (shouldAddRequest(childDataSnapshot.child("servicer_id").getValue().toString())) {
                                requestList.add(new Request(childDataSnapshot));
                                requestListView.setAdapter(customAdapter);
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }
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
                    SharedPreferences preferences = getSharedPreferences("CurrentLocations", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("latitude", String.valueOf(location.getLatitude()));
                    editor.putString("longitude",  String.valueOf(location.getLongitude()));
                    Log.w("TAG", "lat: " + location.getLatitude());
                    editor.commit();
                }
            }
        });

    }

    private boolean shouldAddRequest(String servicerID) {
        SharedPreferences preferences = this.getSharedPreferences("UserSetting", Context.MODE_PRIVATE);
        String userID = preferences.getString("userID", "");

        if (servicerID.equals(userID) || servicerID.equals("open")) {
            return true;
        }
        return false;
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return requestList.toArray().length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.request_card_layout, null);

            TextView textViewSector = (TextView) convertView.findViewById(R.id.textViewSector);
            TextView textViewTipo = (TextView) convertView.findViewById(R.id.textViewTipo);
            TextView textViewDescripcion = (TextView) convertView.findViewById(R.id.textViewServiceDescripcion);
            TextView textViewPrecio = (TextView) convertView.findViewById(R.id.textViewPrecio);
            TextView textIndicator = (TextView) convertView.findViewById(R.id.txtRequestIndicator);
            TextView textBarIndicator = (TextView) convertView.findViewById(R.id.textViewBarIndicator);

            Request[] ts = {};
            requests = requestList.toArray(ts);

            textViewSector.setText(requests[position].getIssueSector());
            textViewTipo.setText(requests[position].getIssueType());
            textViewDescripcion.setText(requests[position].getDescription());
            textViewPrecio.setText(requests[position].getPrice());
            textIndicator.setText("Requerido");
            textIndicator.setTextColor(ContextCompat.getColor(getBaseContext(),  R.color.colorAlert));
            textBarIndicator.setBackgroundColor(ContextCompat.getColor(getBaseContext(),  R.color.colorAlert));

            if (requests[position].getServicerID().equals("open")) {
                textIndicator.setText("Abierto");
                textBarIndicator.setBackgroundColor(ContextCompat.getColor(getBaseContext(),  R.color.colorPrimary));
            }


            return convertView;
        }
    }
}
