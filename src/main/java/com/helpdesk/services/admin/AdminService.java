package com.helpdesk.services.admin;

import java.util.List;

import com.helpdesk.dto.UserDto;

public interface AdminService {

	List<UserDto> getCustomers();
	List<UserDto> getAgents();

}
