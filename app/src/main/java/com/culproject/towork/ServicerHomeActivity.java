package com.culproject.towork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String userData = extras.getString("userData");
        String name = "";
        String role = "";

        //TODO:Pasar esto a un metodo del objeto User.
        try {
            JSONObject usrDataJson = new JSONObject(userData);
            name = usrDataJson.get("name").toString();
            role = usrDataJson.get("role").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        title = (TextView) findViewById(R.id.servicerTitleTextView);
        title.setText("Bienvenido "+name+". Tu rol es de "+ role);


        btnCreateService = (Button) findViewById(R.id.CreateServicesButton);
        btnCreateService.setOnClickListener(createServiceListener);

        btnLookRequests = (Button) findViewById(R.id.LookRequestsButton);
        btnLookRequests.setOnClickListener(lookRequestsListener);

        List<Request> requests = new ArrayList<Request>();
        requests.add(new Request());
        requests.add(new Request());
        requests.add(new Request());


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
}
