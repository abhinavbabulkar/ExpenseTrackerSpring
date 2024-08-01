package com.expense.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.expense.model.JwtResponse;
import com.expense.model.LoginModel;
import com.expense.model.User;
import com.expense.model.UserModel;
import com.expense.security.CustomUserDetailsService;
import com.expense.service.UserService;
import com.expense.util.JwtTokenUtil;

import jakarta.validation.Valid;

@RestController
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody LoginModel loginModel) throws Exception{
		
		authenticate(loginModel.getEmail(),loginModel.getPassword());
		
		//we need to generate jwt token
		final UserDetails userDetails =	userDetailsService.loadUserByUsername(loginModel.getEmail());
		final String token =  jwtTokenUtil.generateToken(userDetails);
		
		return new ResponseEntity<JwtResponse>(new JwtResponse(token) ,HttpStatus.OK);
	}
	
	private void authenticate(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

		} catch (DisabledException e) {
			throw new Exception("User Disabled");
		}
		catch (BadCredentialsException e) {
			throw new Exception("Bad Credentials");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<User> saveUser(@Valid @RequestBody UserModel user){
		return new ResponseEntity<User>(userService.createUser(user),HttpStatus.OK);
	}
}
