package com.sjsu.services;

import java.util.Map;

import com.sjsu.model.Location;



public class RouteService implements IRouteService {

	@Override
	public Map<Location, Integer> findRoute(Map<Double, Double> latLngMap) {
		
		return null;
	}
	
	@Override
	public boolean isSafe(Location loc) {
		//Connect to DB and match values
		
		return false;
	}

	
}
