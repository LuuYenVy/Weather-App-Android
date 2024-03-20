package com.example.project_dbreathuit_app.model.Atrributes;

import com.google.gson.annotations.SerializedName;

public class Temperature{

	@SerializedName("meta")
	private Meta meta;

	@SerializedName("name")
	private String name;

	@SerializedName("type")
	private String type;

	@SerializedName("value")
	private Object value;

	@SerializedName("timestamp")
	private long timestamp;

	public Meta getMeta(){
		return meta;
	}

	public String getName(){
		return name;
	}

	public String getType(){
		return type;
	}

	public Object getValue(){
		return value;
	}

	public long getTimestamp(){
		return timestamp;
	}
}