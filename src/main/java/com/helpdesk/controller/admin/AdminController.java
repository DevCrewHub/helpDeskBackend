package com.helpdesk.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.dto.SignupRequest;
import com.helpdesk.dto.UserDto;
import com.helpdesk.services.admin.AdminService;
//import com.helpdesk.services.admin.RegisterAgentService;
import com.helpdesk.services.auth.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
	
	private final AdminService adminService;
	private final AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<?> signupAgent(@RequestBody SignupRequest signupRequest) {
		if (authService.hasUserWithUsername(signupRequest.getUserName())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Agent already exists with this username");
		}
		if (authService.hasUserWithEmail(signupRequest.getEmail())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Agent already exists with this email");
		}
		UserDto createdUserDto = authService.signupAgent(signupRequest);
		if (createdUserDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Agent not created");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
	}
	
	// Get all customers
	@GetMapping("/customers")
	public ResponseEntity<?> getCustomers() {
		return ResponseEntity.ok(adminService.getCustomers());
	}
	
	// Get all agents
	@GetMapping("/agents")
	public ResponseEntity<?> getAgents() {
		return ResponseEntity.ok(adminService.getAgents());
	}

}
