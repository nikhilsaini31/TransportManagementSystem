package com.TransportManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TransportManagementSystem.Entity.User;
import com.TransportManagementSystem.Repo.UserRepository;
import com.TransportManagementSystem.exception.loginException;
import com.TransportManagementSystem.payload.JwtAuthRequest;
import com.TransportManagementSystem.payload.JwtAuthResponse;
import com.TransportManagementSystem.security.JwtTokenHelper;
import com.TransportManagementSystem.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest jwtAuthRequest) throws Exception {

		this.authenticate(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword());
		
		UserDetails userByUsername = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUsername());
		
	    String token = this.jwtTokenHelper.generateToken(userByUsername);

	   // get user for getting user role
	    User user = this.userRepository.findByEmail(userByUsername.getUsername()).orElseThrow(()-> new RuntimeException("user not found"));
       // System.out.println("_______ = "+user);
	    
	    String role = user.getRole();
	    
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		jwtAuthResponse.setToken(token);
		jwtAuthResponse.setRole(role);
			
		return new ResponseEntity<JwtAuthResponse>(jwtAuthResponse, HttpStatus.OK);

	}

	private void authenticate(String username, String password) throws Exception {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		

		try {

			this.authenticationManager.authenticate(authenticationToken);

		} catch (BadCredentialsException e) {
			System.out.println("Incorrect username or password");
		  throw new loginException();
			
		}

	}
 
	// register API

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) {

		User registeredUser = this.userService.registerNewUser(user);

		return new ResponseEntity<User>(registeredUser, HttpStatus.CREATED);
	}

}
