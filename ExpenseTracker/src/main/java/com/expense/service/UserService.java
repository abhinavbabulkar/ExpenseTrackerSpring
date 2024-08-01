package com.expense.service;

import com.expense.model.User;
import com.expense.model.UserModel;

public interface UserService {
	User createUser(UserModel user);
	
	User readUser();
	
	User updateUser(UserModel user);
	
	void deleteUser();
	
	User getLoggedInUser();
}
