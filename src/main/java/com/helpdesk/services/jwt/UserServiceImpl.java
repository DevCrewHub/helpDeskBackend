package com.helpdesk.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.helpdesk.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Generates a constructor with required arguments (final fields)
public class UserServiceImpl implements UserService {

    // Repository to fetch user data from the database
    private final UserRepository userRepository;

    /**
     * Provides a UserDetailsService implementation used by Spring Security to load user-specific data.
     * 
     * @return UserDetailsService instance with custom user loading logic
     */
    @Override
    public UserDetailsService userDetailService() {
        return new UserDetailsService() {
            /**
             * Loads user by username from the database.
             * 
             * @param username the username to search for
             * @return UserDetails object if found
             * @throws UsernameNotFoundException if user is not found
             */
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByUserName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

}
