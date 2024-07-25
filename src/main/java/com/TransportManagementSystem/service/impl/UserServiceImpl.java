package com.TransportManagementSystem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.TransportManagementSystem.Entity.User;
import com.TransportManagementSystem.Repo.UserRepository;
import com.TransportManagementSystem.exception.ResourceNotFoundException;
import com.TransportManagementSystem.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User save(User user) {
		
		User savedUser = this.userRepo.save(user);
		return savedUser;
	}

	@Override
	public List<User> getAll() {
		
		List<User> list = this.userRepo.findAll();
		return list;
		
	}

	@Override
	public User getSingle(Long id) {
		
		User user = this.userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user", "id", id));
		return user;
		
	}

	@Override
	public User updateUser(User user, Long id) {
		
		User oldUser = this.userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user", "id", id));
		
		oldUser.setFirstName(user.getFirstName());
		oldUser.setSecondName(user.getSecondName());
		oldUser.setEmail(user.getEmail());
		oldUser.setPassword(user.getPassword());
		oldUser.setPhone(user.getPhone());
		oldUser.setAddress(user.getAddress());
		oldUser.setCity(user.getCity());
		oldUser.setState(user.getState());
		oldUser.setCountry(user.getCountry());
		
		User updatedUser = this.userRepo.save(oldUser);
		
		return updatedUser;
	}

	@Override
	public void deleteUser(Long id) {
		
		User user = this.userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user", "id", id));
        
		this.userRepo.delete(user);
	}

	@Override
	public User registerNewUser(User user) {
		
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		
		user.setRole(user.getRole());
		User user2 = this.userRepo.save(user);
		return user2;
	}

}
