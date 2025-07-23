package com.helpdesk.dto;

import com.helpdesk.enums.UserRole;
import lombok.Data;

@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class UserDto {

    // Unique identifier for the user
    private Long id;

    // Username of the user (used for login)
    private String userName;

    // Email address of the user
    private String email;

    // Password of the user (should be stored in encrypted form)
    private String password;

    // Full name of the user
    private String fullName;

    // Contact number of the user
    private String phoneNumber;

    // Role of the user (e.g., ADMIN, AGENT, CUSTOMER)
    private UserRole userRole;

    // ID of the department the user is assigned to (for agents)
    private Long departmentId;

    // Name of the department the user is assigned to
    private String departmentName;
}
