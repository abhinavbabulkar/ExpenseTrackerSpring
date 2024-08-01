package com.expense.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expense.exceptions.ItemAlreadyExistsException;
import com.expense.exceptions.ResourceNotFoundException;
import com.expense.model.User;
import com.expense.model.UserModel;
import com.expense.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bCryptEncoder;
	
	@Override
	public User createUser(UserModel user) {
		
		if(userRepository.existsByEmail(user.getEmail()))
			throw new ItemAlreadyExistsException("User is already registered with email: " + user.getEmail());
		
		User finUser = new User();
		
		BeanUtils.copyProperties(user, finUser);
		finUser.setPassword(bCryptEncoder.encode(finUser.getPassword()));
		return userRepository.save(finUser);
	}

	@Override
	public User readUser() {
		Long userId = getLoggedInUser().getId();
		return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not found for the id: " + userId));
	}

	@Override
	public User updateUser(UserModel user) throws ResourceNotFoundException {
		User existingUser = readUser();
		
		existingUser.setName(user.getName()!=null ? user.getName() : existingUser.getName());
		existingUser.setEmail(user.getEmail()!=null ? user.getEmail() : existingUser.getEmail());
		existingUser.setPassword(user.getPassword()!=null ? bCryptEncoder.encode(user.getPassword()) : existingUser.getPassword());
		existingUser.setAge(user.getAge()!=null ? user.getAge() : existingUser.getAge());
		return userRepository.save(existingUser);
	}

	@Override
	public void deleteUser() {
		User user = readUser();
		userRepository.delete(user);
	}

	@Override
	public User getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email =  authentication.getName();
		
		return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found for the email: "+ email));
		
	}
	
	
	
}
