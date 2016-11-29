package com.sjsu.controller;

/**
 * 
 */

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import com.sjsu.model.UserRequest;
import com.sjsu.services.LoginServiceImpl;
import com.sjsu.services.RedisDBServiceImpl;

@Controller

public class SignupController {
	
	UserRequest req = new UserRequest();
	LoginServiceImpl loginImpl = new com.sjsu.services.LoginServiceImpl();
	@RequestMapping(value="signup")
    public ModelAndView home() {
		//System.out.println(user);
		
		ModelAndView modelandView 
       = new ModelAndView("signup", "user", new RedisDBServiceImpl());
		
		return modelandView;

    }

	@RequestMapping(value="signup1")
	
    public ModelAndView home1(@ModelAttribute("user")UserRequest user,
   		   BindingResult result) {
		System.out.println(user);
		boolean success = false;
		ModelAndView model = new ModelAndView("search");
		
		try {
			success = LoginServiceImpl.selectRecordFromDb(user.getUserID(), user.getUserID());
			if(success) {
				
				return new ModelAndView("login");
				
			}
			else {
			    return new ModelAndView("errorPage");
				
				//System.out.println("Sign-up failed.\n");
			}}
		catch (SQLException e) {
			e.printStackTrace();
		}return null;}
}
	
	
	