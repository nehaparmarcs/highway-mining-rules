package com.sjsu.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.sjsu.constants.ApplicationConstants;
import com.sjsu.model.Location;

import static com.sjsu.constants.ApplicationConstants.SAFETY_HIGH;
import static com.sjsu.constants.ApplicationConstants.SAFETY_LOW;
import static com.sjsu.constants.ApplicationConstants.SAFETY_DANGEROUS;
import static com.sjsu.constants.ApplicationConstants.SAFETY_SEVERE;
import com.sjsu.services.RouteService;

@Controller
public class SafeStateController {
	

	@RequestMapping(value = "/findRoute", method = RequestMethod.POST)
	@ResponseBody
	public String findRoute(@RequestBody String reqData, Model model) {
		RouteService routeService = new RouteService();
		
		System.out.println("Entered find route method: " + reqData);
		
		JSONObject jsonObject = new JSONObject(reqData);
		String userid = jsonObject.getString("userid");
		String id = jsonObject.getString("id");
		String name = jsonObject.getString("userName");
		String searchText = jsonObject.getString("searchText");
		Double source = jsonObject.getDouble("source");
		Double destination = jsonObject.getDouble("destination");
		
		Map<Double, Double> latLngMap = session.getAttribute("latLngMap");
		
		Map<Location, Integer> responseMap = routeService.findRoute(latLngMap);
		Map<Location, Integer> responseMapToSend = new HashMap<Location, Integer>(); 
		Set<Location> locationSet  = responseMap.keySet();
		
		for(Location loc: locationSet){
			//Chk for safety if Vulnerable add to responseMap
			boolean safe = routeService.isSafe(loc);
			if(!safe){
				Integer resValue = responseMap.get(loc);
				switch (resValue){
				case 1: 
					responseMapToSend.put(loc, SAFETY_LOW);
					break;
				case 2: 
					responseMapToSend.put(loc, SAFETY_SEVERE);
					break;
				case 3: 
					responseMapToSend.put(loc, SAFETY_SEVERE);
					break;
				case 4: 
					responseMapToSend.put(loc, SAFETY_DANGEROUS);
					break;
				}
				
				
			}
			
		}
		
		if(responseMapToSend!=null)
			model.addAttribute(responseMapToSend);
		

		return String.valueOf(responseMapToSend.size());
	}



}