package com.helpdesk.services.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service interface for managing user-related operations for JWT authentication.
 */
public interface UserService {

    /**
     * Returns a custom implementation of Spring Security's UserDetailsService,
     * which is used to load user-specific data during authentication.
     * 
     * @return a UserDetailsService implementation
     */
    UserDetailsService userDetailService();

}
