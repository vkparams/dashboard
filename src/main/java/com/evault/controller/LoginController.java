package com.evault.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String welcome(ModelMap model) {

		model.addAttribute("message",
				"Maven Web Project + Spring 3 MVC - welcome()");

		// Spring uses InternalResourceViewResolver and return back index.jsp
		return "login";

	}

	
	
    @RequestMapping(value = "/verifylogin", method = RequestMethod.POST)
    public String addContact(@RequestParam("username")String username, @RequestParam("password")String password) {
//
//        System.out.println(" Name:" +username +
//                    "password: " + password);
//        SkylineUtils utils=new SkylineUtils();
//        String token=utils.getAuthTokens(username,password);
//        System.out.println("token = "+token);
        
        return "redirect:/listcontainers?token="+"";
    }
    
    
	@RequestMapping(value = "/welcome/{name}", method = RequestMethod.GET)
	public String welcomeName(@PathVariable String name, ModelMap model) {

		model.addAttribute("message", "Maven Web Project + Spring 3 MVC - "
				+ name);
		return "index";

	}

}