package com.sjsu.model;


import java.util.HashMap;
import java.util.Map;

/**
 * Represents a User input information throughout the application.
 * RequestManager uses this object to interact with all the services.
 * 
 * @author neha
 */
public class UserRequest {

	private String userID;
	private String userName;
	private String searchText;
	private String source;
	private String destination;
	private Map<Double, Double> latLngMap;
		
	public UserRequest() {
		super();
		latLngMap = new HashMap<Double, Double>();
		
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

	public Map<Double, Double> getLatLngList() {
		return latLngMap;
	}

	public void setLatLngList(Map<Double, Double> latLngList) {
		this.latLngMap = latLngList;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	
}
