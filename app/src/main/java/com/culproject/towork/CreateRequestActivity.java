package com.culproject.towork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CreateRequestActivity extends AppCompatActivity {

    private Spinner spnProblemType;
    private Spinner spnProblemSector;
    private EditText editTxtPrice;
    private EditText editTxtDescription;
    private FirebaseAuth firebaseAuth;

    String servicerID = "open";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        firebaseAuth = FirebaseAuth.getInstance();

        try {
            String servicerData = getIntent().getStringExtra("servicerID");
            if (servicerData.isEmpty() == false){
                servicerID = servicerData;
            }
        } catch (Exception err) {  Log.w("TAG", "free service"); }



        spnProblemType = (Spinner) findViewById(R.id.problemTypeRequestSpinner);
        spnProblemSector = (Spinner) findViewById(R.id.problemSectorRequestSpinner);
        editTxtPrice = (EditText) findViewById(R.id.requestPriceEditText);
        editTxtDescription = (EditText) findViewById(R.id.requestDescriptionMultiline);


        ArrayAdapter<CharSequence> problemSectorAdapter = ArrayAdapter.createFromResource(this, R.array.proplem_sector_array, android.R.layout.simple_spinner_item);
        problemSectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> problemSectorTypeAdapter = ArrayAdapter.createFromResource(this, R.array.problem_sector_compute_array, android.R.layout.simple_spinner_item);
        problemSectorTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnProblemType.setAdapter(problemSectorAdapter);
        spnProblemSector.setAdapter(problemSectorTypeAdapter);
    }

    public void crearSolicitud(View view) {


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("requests");

        String requesID = getRequestId();

        myRef.child(requesID).child("description").setValue(editTxtDescription.getText().toString());
        myRef.child(requesID).child("price").setValue(editTxtPrice.getText().toString());
        myRef.child(requesID).child("issue_sector").setValue(spnProblemSector.getSelectedItem().toString());
        myRef.child(requesID).child("issue_type").setValue(spnProblemType.getSelectedItem().toString());
        myRef.child(requesID).child("servicer_id").setValue(servicerID);

        SharedPreferences preferences = this.getSharedPreferences("UserSetting", Context.MODE_PRIVATE);
        String userID = preferences.getString("userID", "");

        myRef.child(requesID).child("user_id").setValue(userID);

        Toast.makeText(this, "Solicitud Enviada", Toast.LENGTH_LONG).show();

        finish();
    }

    private String getRequestId() {
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId;
    }



}
