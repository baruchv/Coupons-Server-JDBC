package com.baruch.coupons.api;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baruch.coupons.beans.CensoredUser;
import com.baruch.coupons.beans.LoginDetails;
import com.baruch.coupons.beans.SuccessfulLoginData;
import com.baruch.coupons.beans.User;
import com.baruch.coupons.enums.UserType;
import com.baruch.coupons.exceptions.ApplicationException;
import com.baruch.coupons.logic.UsersController;

@RestController
@RequestMapping("/users")
public class UsersApi {
	
	@Autowired
	private UsersController controller;
	
	public UsersApi() {
	}
	
	@GetMapping("/{userID}")
	public CensoredUser getUser(@PathVariable("userID") long id) throws ApplicationException {
		return controller.getUser(id);
	}
	
	@GetMapping
	public List<CensoredUser> getAllUsers() throws ApplicationException{
		return controller.getAllUsers();	
	}
	
	@GetMapping("/byType")
	public List<CensoredUser> getUsersByType(@RequestParam("type") UserType type) throws ApplicationException{
		return controller.getUsersByType(type);
	}
	
	@GetMapping("/byCompany")
	public List<CensoredUser	> getUsersByCompany(@RequestParam("companyID") long companyID) throws ApplicationException{
		return controller.getUsersByCompany(companyID);	
	}	
	
	@DeleteMapping("/{userID}")
	public void deleteUser(@PathVariable("userID") long id) throws ApplicationException{
		controller.deleteUser(id);
	}
	
	@PostMapping
	public void createUser(@RequestBody User user) throws ApplicationException {
		controller.createUser(user);
	}
	
	@PostMapping("/login")
	public SuccessfulLoginData login(@RequestBody LoginDetails loginDetails) throws ApplicationException{
		return controller.login(loginDetails.getUserName(), loginDetails.getPassword());
	}
	
	@PostMapping("/logout")
	public void logout(@RequestHeader("Authorization") String token) {
		controller.logout(token);
		
	}
	
	@PutMapping
	public void updateUser(@RequestBody User user) throws ApplicationException {
		controller.updateUser(user);
	}
	
}	
