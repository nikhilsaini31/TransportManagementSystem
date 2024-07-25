package com.TransportManagementSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> notFoundException(ResourceNotFoundException ex) {
		
		String message = ex.getMessage();
		ExceptionResponse response = new ExceptionResponse(message,false);
		
		return new ResponseEntity<ExceptionResponse>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(loginException.class)
	public ResponseEntity<ExceptionResponse> apiException(loginException ex){
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse("Invalid username or password", false));
	}
	
}
