package com.helpdesk.dto;

import lombok.Data;

/**
 * DTO for capturing user signup details from the client.
 */
@Data  // Lombok annotation to auto-generate getters, setters, toString, equals, and hashCode
public class SignupRequest {

    // Username chosen by the user
    private String userName;

    // User's email address
    private String email;

    // Password provided during signup
    private String password;

    // Full name of the user
    private String fullName;

    // Contact phone number of the user
    private String phoneNumber;

    // Role of the user (e.g., ADMIN, AGENT, CUSTOMER)
    private String userRole;

    // Name of the department the user belongs to
    private String departmentName;
}
