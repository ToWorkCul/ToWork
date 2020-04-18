package com.culproject.towork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SeekerHomeActivity extends AppCompatActivity {

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_home);

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

        title = (TextView) findViewById(R.id.seekerTitleTextView);
        title.setText("Bienvenido "+name+". Tu rol es de "+ role);
    }

}
