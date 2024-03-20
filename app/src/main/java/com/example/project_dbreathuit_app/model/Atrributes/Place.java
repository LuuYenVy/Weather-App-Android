package com.example.project_dbreathuit_app.model.Atrributes;

import com.google.gson.annotations.SerializedName;

public class Place{

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("value")
    private String value;

    @SerializedName("timestamp")
    private long timestamp;

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public String getValue(){
        return value;
    }

    public long getTimestamp(){
        return timestamp;
    }
}
