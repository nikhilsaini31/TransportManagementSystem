package com.TransportManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.TransportManagementSystem.Entity.User;
import com.TransportManagementSystem.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping()
	public ResponseEntity<User> save(@RequestBody User user){
		
		User savedUser = userService.save(user);
		return new ResponseEntity<User>(savedUser,HttpStatus.CREATED);
	}
	
	@GetMapping()
	public ResponseEntity<List<User>> getAll(){
		
		List<User> all = userService.getAll();
		return new ResponseEntity<List<User>>(all,HttpStatus.FOUND);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getSingle(@PathVariable("id") Long id){
		
		User user = userService.getSingle(id);
		
		return new ResponseEntity<User>(user,HttpStatus.FOUND);
	}
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<User> updateUser(@RequestBody User user,@PathVariable("id") Long id){
		
		User updateUser = userService.updateUser(user, id);
		return new ResponseEntity<User>(updateUser,HttpStatus.OK);
		
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id){
		
		userService.deleteUser(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}
