package com.culproject.towork;

import com.google.firebase.database.DataSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Request{
    private String description = "Descripcion";
    private String price = "$200000";
    private String issueType = "Hardware";
    private String issueSector = "Equipo de computo";
    private String servicerID = "open";
    private String userID = "";
    private String summary = "CPU no enciende";

    public Request(){}

    public Request(JSONObject data) throws JSONException {
        description = data.getString("description");
        price = data.getString("price");
        issueType = data.getString("issueType");
        summary = data.getString("summary");
    }

    public Request(DataSnapshot data) throws JSONException {
        description = data.child("description").getValue().toString();
        issueSector = data.child("issue_sector").getValue().toString();
        issueType = data.child("issue_type").getValue().toString();
        price = data.child("price").getValue().toString();
        servicerID = data.child("servicer_id").getValue().toString();
        userID = data.child("user_id").getValue().toString();
        summary = data.child("description").getValue().toString();
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getIssueType() {
        return issueType;
    }

    public String getSummary() {
        return summary;
    }

    public String getIssueSector() {
        return issueSector;
    }

    public String getServicerID() {
        return servicerID;
    }

    public String getUserID() {
        return userID;
    }
}
