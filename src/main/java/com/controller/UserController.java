package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.entity.Users;
import com.service.EmailService;
import com.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@GetMapping("/login")
	public String login(Users user, HttpSession session) {
		session.setAttribute("user", user);
		return "login";
	}
	
//	@PostMapping("/registeruser")
//	public String registeruser(Users user,RedirectAttributes redirectAttributes) {
//		 System.out.println("Register endpoint hit"); 
//		Users saveusers = userService.saveusers(user);
//		if(!ObjectUtils.isEmpty(saveusers)) {
//			redirectAttributes.addFlashAttribute("succmsg", "Register Successfully"); 
//		}else {
//			redirectAttributes.addFlashAttribute("errormsg", "Register Failed"); 
//		}
//		return "redirect:/register";
//	}
	
	@Autowired
	private EmailService emailService;

	@PostMapping("/registeruser")
	public String registeruser(Users user, RedirectAttributes redirectAttributes) {
	    System.out.println("Register endpoint hit"); 

	    Users saveusers = userService.saveusers(user);

	    if (!ObjectUtils.isEmpty(saveusers)) {
	        // Send email after successful registration
	        emailService.sendRegistrationEmail(
	            saveusers.getEmail(),       
	            saveusers.getName(),        
	            user.getPassword()          
	        );

	        redirectAttributes.addFlashAttribute("succmsg", "Register Successfully. Please check your email.");
	    } else {
	        redirectAttributes.addFlashAttribute("errormsg", "Register Failed");
	    }

	    return "redirect:/register";
	}
}
