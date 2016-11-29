package com.sjsu.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.sjsu.model.Location;
import com.sjsu.model.UserRequest;
import com.sjsu.services.LoginServiceImpl;

@Controller
@SessionAttributes("routes")
public class DetailsController {

	@RequestMapping(value="details")
    public ModelAndView home(@RequestParam(value = "LocationID", required = false) String LocationID,
    		@ModelAttribute("routes")ArrayList<Location> routes) {
		Location detailLocation = new Location() ;
		for(Location Location: routes){
			if(LocationID.equals(Location.getLat())){
				detailLocation = Location;
			}
		}
		
		ModelAndView modelandView = new ModelAndView("detailsPage", "commandLocation", routes);
		modelandView.addObject("detailLocation", detailLocation);
		return modelandView;
		
    }

	@RequestMapping(value="routeDetailsPage")
   public ModelAndView getRouteDetails(@ModelAttribute("user")UserRequest user,
  		   BindingResult result) {
		System.out.println(user);
		boolean success = false;
		ModelAndView model = new ModelAndView("search");
		
		try {
			
			//Keeping user ID and Password as same!
			success = LoginServiceImpl.selectRecordFromDb(user.getUserID(),user.getUserID());
			if(success) {
			
				
			}
			else {
			    return new ModelAndView("errorPage");
			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
   }
}
