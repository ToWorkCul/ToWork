package com.culproject.towork;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Request{
    private String description = "Descripcion";
    private String price = "$200000";
    private String issueType = "Hardware";
    private String summary = "CPU no enciende";

    public Request(){}

    public Request(JSONObject data) throws JSONException {
        description = data.getString("description");
        price = data.getString("price");
        issueType = data.getString("issueType");
        summary = data.getString("summary");
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
}
