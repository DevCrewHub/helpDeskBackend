package com.helpdesk.dto;

import lombok.Data;

/**
 * DTO for handling authentication requests.
 * It contains the username and password provided by the user during login.
 */
@Data  // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class AuthenticationRequest {

    // Username provided by the user during login
    private String userName;

    // Password provided by the user during login
    private String password;

}
