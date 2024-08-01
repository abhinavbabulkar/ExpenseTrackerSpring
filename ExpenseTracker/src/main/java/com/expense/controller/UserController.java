 package com.expense.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense.exceptions.ResourceNotFoundException;
import com.expense.model.User;
import com.expense.model.UserModel;
import com.expense.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	
	@GetMapping("/profile")
	public ResponseEntity<User> readUser(){
		return new ResponseEntity<User>(userService.readUser(), HttpStatus.OK);
	}
	
	@PutMapping("/profile")
	public ResponseEntity<User> updateUser(@RequestBody UserModel user){
		return new ResponseEntity<User>(userService.updateUser(user), HttpStatus.OK);
	}
	
	@DeleteMapping("/deactivate/{id}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
		userService.deleteUser();
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
	
}
