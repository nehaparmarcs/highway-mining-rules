package com.sjsu.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This will collect final response from all services. 
 * It will store the final order placed and Status.
 * @author navdeepdahiya
 */

public class UserResponse {

	private String userID;
	private String userName;
	private boolean status;
	
	private Map<Double, Double> latLngresponseMap;
	


	public UserResponse() {
		super();
		latLngresponseMap = new HashMap<Double, Double>();
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Map<Double, Double> getLatLngresponseMap() {
		return latLngresponseMap;
	}

	public void setLatLngresponseMap(Map<Double, Double> latLngresponseMap) {
		this.latLngresponseMap = latLngresponseMap;
	}
	
}
