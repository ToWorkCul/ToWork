package com.culproject.towork;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Request{
    private String description;
    private String price;
    private String issueType;
    private String summary;

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
