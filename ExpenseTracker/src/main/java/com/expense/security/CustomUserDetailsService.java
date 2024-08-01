package com.expense.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.expense.model.User;
import com.expense.repository.UserRepository;


@Service
 public class CustomUserDetailsService implements UserDetailsService {

	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User existingUser = userRepository.findByEmail(username)
				.orElseThrow(()-> new UsernameNotFoundException("User is not present with email: " + username));
	
		return new org.springframework.security.core.userdetails.User(existingUser.getEmail(), existingUser.getPassword(),
				new ArrayList<>());
		
	}

}
