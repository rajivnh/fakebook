package com.tutorial.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.jwt.JwtService;
import com.tutorial.jwt.JwtStore;
import com.tutorial.model.AuthRequest;
import com.tutorial.model.AuthResponse;
import com.tutorial.persistence.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {
	@Autowired
	JwtService jwtService;
	
	@Autowired
	AuthService authService;
	
	@Autowired
	HttpServletRequest request;
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) throws SQLException {
		AuthResponse response = authService.findByEmailId(request.getEmailId(), request.getPassword());
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/signout")
	public ResponseEntity<Void> authenticate() {
		final String authHeader = request.getHeader("Authorization");
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		final String accessToken = authHeader.substring(7);
		final String emailId = jwtService.extractUsername(accessToken);
	    
		JwtStore.remove(emailId);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
