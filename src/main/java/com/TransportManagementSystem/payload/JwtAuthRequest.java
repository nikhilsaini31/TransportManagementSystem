package com.TransportManagementSystem.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthRequest {

	private String username; 

	private String password;

}
