package com.culproject.towork;

import com.google.firebase.database.DataSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

public class Service {
    private String id = "000000";
    private String name = "El Nombre";
    private String description = "Descripcion";
    private String lat = "20000.0";
    private String lon = "00000.0";


    public Service(){}

    public Service(JSONObject data) throws JSONException {
        id = data.getString("id");
        name = data.getString("name");
        description = data.getString("description");
        lat = data.getString("lat");
        lon = data.getString("lon");
    }

    public Service(DataSnapshot data) throws JSONException {
        id = data.child("id").getValue().toString();
        name = data.child("name").getValue().toString();
        description = data.child("description").getValue().toString();
        lat = data.child("lat").getValue().toString();
        lon = data.child("lon").getValue().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
