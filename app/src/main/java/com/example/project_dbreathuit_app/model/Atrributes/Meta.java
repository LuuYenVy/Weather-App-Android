package com.example.project_dbreathuit_app.model.Atrributes;

import com.google.gson.annotations.SerializedName;

public class Meta{

	@SerializedName("readOnly")
	private boolean readOnly;

	@SerializedName("storeDataPoints")
	private boolean storeDataPoints;

	@SerializedName("showOnDashboard")
	private boolean showOnDashboard;

	@SerializedName("label")
	private String label;

	public boolean isReadOnly(){
		return readOnly;
	}

	public boolean isStoreDataPoints(){
		return storeDataPoints;
	}

	public boolean isShowOnDashboard(){
		return showOnDashboard;
	}

	public String getLabel(){
		return label;
	}
}