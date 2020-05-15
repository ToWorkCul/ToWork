package com.culproject.towork;

import com.google.firebase.database.DataSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

public class Service {
    private String _id = "000000";
    private String servicerID = "";
    private String _name = "El Nombre";
    private String _description = "Descripcion";
    private String _lat = "20000.0";
    private String _lon = "00000.0";


    public Service(){}

    public Service(JSONObject data) throws JSONException {
        _id = data.getString("id");
        _name = data.getString("name");
        _description = data.getString("description");
        _lat = data.getString("lat");
        _lon = data.getString("lon");
        servicerID = data.getString("servicer_id");
    }

    public Service(DataSnapshot data) throws JSONException {
        _id = data.child("id").getValue().toString();
        _name = data.child("name").getValue().toString();
        _description = data.child("description").getValue().toString();
        _lat = data.child("lat").getValue().toString();
        _lon = data.child("lon").getValue().toString();
        servicerID = data.child("servicer_id").getValue().toString();
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getDescription() {
        return _description;
    }

    public String getLat() {
        return _lat;
    }

    public String getLon() {
        return _lon;
    }

    public String getServicerID() {
        return servicerID;
    }
}
