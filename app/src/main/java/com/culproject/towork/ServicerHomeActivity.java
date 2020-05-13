package com.culproject.towork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private Button btnCreateService;
    private Button btnLookRequests;
    private FirebaseAuth firebaseAuth;

    private Map<String, Object> usersData;
    Request[] requests = {new Request(), new Request(), new Request(), new Request(), new Request(), new Request()};

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

        title = (TextView) findViewById(R.id.servicerTitleTextView);

        firebaseAuth = FirebaseAuth.getInstance();

        btnCreateService = (Button) findViewById(R.id.CreateServicesButton);
        btnCreateService.setOnClickListener(createServiceListener);

        btnLookRequests = (Button) findViewById(R.id.LookRequestsButton);
        btnLookRequests.setOnClickListener(lookRequestsListener);

        ListView requestListView = (ListView)findViewById(R.id.servicerRequestListView);
        CustomAdapter customAdapter = new CustomAdapter();
        requestListView.setAdapter(customAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("requests");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {

                Log.w("TAG", "Exitoso: Data: " + data);
                Log.w("TAG", "E---->: " + data.getKey());

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
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return requests.length;
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

            textViewSector.setText("Equipos de Computos");
            textViewTipo.setText("Hardware(Fisico)");
            textViewDescripcion.setText(requests[position].getDescription());
            textViewPrecio.setText(requests[position].getPrice());

            return convertView;
        }
    }
}
