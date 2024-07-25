package com.TransportManagementSystem.service;

import java.util.List;

import com.TransportManagementSystem.Entity.User;

public interface UserService {

	User save(User user);
	
	List<User> getAll();
	
	User getSingle(Long id);
	
	User updateUser(User user,Long id);
	
	void deleteUser(Long id);

	User registerNewUser(User user);
	
}
