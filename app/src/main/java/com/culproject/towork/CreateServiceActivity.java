package com.culproject.towork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CreateServiceActivity extends AppCompatActivity {
    private Spinner spnProblemType;
    private Spinner spnProblemSector;

    private EditText editTxtDescription;
    private EditText editTxtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);

        editTxtDescription = (EditText) findViewById(R.id.serviceDescriptionMultiline);
        editTxtName = (EditText) findViewById(R.id.editTxtserviceName);
    }

    public void createService(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("services");

        SharedPreferences currentLocationpreferences = this.getSharedPreferences("CurrentLocations", Context.MODE_PRIVATE);
        String latitude = currentLocationpreferences.getString("latitude", "");
        String longitude = currentLocationpreferences.getString("longitude", "");

        String requesID = getRequestId();

        myRef.child(requesID).child("description").setValue(editTxtDescription.getText().toString());
        myRef.child(requesID).child("name").setValue(editTxtName.getText().toString());
        myRef.child(requesID).child("id").setValue(getRequestId());
        myRef.child(requesID).child("lat").setValue(latitude);
        myRef.child(requesID).child("lon").setValue(longitude);

        SharedPreferences preferences = this.getSharedPreferences("UserSetting", Context.MODE_PRIVATE);
        String userID = preferences.getString("userID", "");

        myRef.child(requesID).child("servicer_id").setValue(userID);

        Toast.makeText(this, "Servicio Creado", Toast.LENGTH_LONG).show();

        finish();
    }

    private String getRequestId() {
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId;
    }
}
