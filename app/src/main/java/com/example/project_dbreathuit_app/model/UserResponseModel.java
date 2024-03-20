package com.example.project_dbreathuit_app.model;

import com.google.gson.annotations.SerializedName;

public class UserResponseModel {

	@SerializedName("firstName")
	public String firstName;

	@SerializedName("lastName")
	public String lastName;

	@SerializedName("realmId")
	public String realmId;

	@SerializedName("realm")
	public String realm;

	@SerializedName("serviceAccount")
	public boolean serviceAccount;

	@SerializedName("id")
	public String id;

	@SerializedName("createdOn")
	public String createdOn;

	@SerializedName("email")
	public String email;

	@SerializedName("enabled")
	public String enabled;

	@SerializedName("username")
	public String username;

	public String getFirstName(){
		return firstName;
	}

	public String getLastName(){
		return lastName;
	}

	public String getRealmId(){
		return realmId;
	}

	public String getRealm(){
		return realm;
	}

	public boolean isServiceAccount(){
		return serviceAccount;
	}

	public String getId(){
		return id;
	}

	public String getCreatedOn(){
		return createdOn;
	}

	public String getEmail(){
		return email;
	}

	public String isEnabled(){
		return enabled;
	}

	public String getUsername(){
		return username;
	}
}