package com.helpdesk.dto;

import com.helpdesk.enums.UserRole;
import lombok.Data;

/**
 * DTO for sending authentication response data after successful login.
 * Contains the JWT token, user ID, and the role of the authenticated user.
 */
@Data  // Lombok annotation to auto-generate getters, setters, toString, equals, and hashCode methods
public class AuthenticationResponse {

    // JWT token generated after successful authentication
    private String jwt;

    // ID of the authenticated user
    private long userId;

    // Role of the authenticated user (e.g., ADMIN, AGENT, CUSTOMER)
    private UserRole userRole;

}
