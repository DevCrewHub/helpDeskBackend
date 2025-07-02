package com.helpdesk.services.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.UserDto;
import com.helpdesk.entities.User;
import com.helpdesk.enums.UserRole;
import com.helpdesk.repositories.UserRepository;
import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Override
	public List<UserDto> getCustomers() {
		return userRepository.findAll().stream().filter(user -> {
			UserRole role = user.getUserRole();
			return role == UserRole.CUSTOMER;
		}).map(User::getUserDto).collect(Collectors.toList());
	}
	
	@Override
	public List<UserDto> getAgents() {
		return userRepository.findAll().stream().filter(user -> {
			UserRole role = user.getUserRole();
			return role == UserRole.AGENT;
		}).map(User::getUserDto).collect(Collectors.toList());
	}
	
}