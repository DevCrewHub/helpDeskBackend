package com.helpdesk.services.auth;

import com.helpdesk.dto.SignupRequest;
import com.helpdesk.dto.UserDto;

/**
 * AuthService defines the authentication-related operations
 * such as user/agent registration and checking user existence.
 */
public interface AuthService {

    /**
     * Registers a new user with CUSTOMER role.
     *
     * @param signupRequest the user registration request data
     * @return the registered user's data
     */
    UserDto signupUser(SignupRequest signupRequest);

    /**
     * Registers a new user with AGENT role.
     *
     * @param signupRequest the agent registration request data
     * @return the registered agent's data
     */
    UserDto signupAgent(SignupRequest signupRequest);

    /**
     * Checks if a user exists with the given username.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    boolean hasUserWithUsername(String username);

    /**
     * Checks if a user exists with the given email.
     *
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    boolean hasUserWithEmail(String email);
}
