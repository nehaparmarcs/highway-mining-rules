package com.sjsu.services;

import java.util.Map;

import com.sjsu.model.Location;
import com.sjsu.model.UserRequest;
import com.sjsu.model.UserResponse;

/**
 * Allows shoppingManager to browse Item on the basis of searchText entered.
 * 
 *
 */
public interface IRouteService {

	/**
	 * Find various routes and perceived safety levels on them 
	 * @param latLngMap
	 * @return mappings for routes
	 */
	public Map<Location, Integer> findRoute(Map<Double, Double> latLngMap);
	
	/**
	 * Returns the safety level for unique Location
	 * @param loc
	 * @return TODO
	 */
	public boolean isSafe(Location loc);
	
}
