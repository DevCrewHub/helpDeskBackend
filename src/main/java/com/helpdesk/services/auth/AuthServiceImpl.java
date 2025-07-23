package com.helpdesk.services.auth;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.helpdesk.dto.SignupRequest;
import com.helpdesk.dto.UserDto;
import com.helpdesk.entities.Department;
import com.helpdesk.entities.User;
import com.helpdesk.enums.UserRole;
import com.helpdesk.repositories.DepartmentRepository;
import com.helpdesk.repositories.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // Dependency injections via constructor
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    /**
     * Automatically creates an admin account after the service is initialized,
     * if one doesn't already exist.
     */
    @PostConstruct
    public void createAnAdminAccount() {
        Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);
        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setFullName("admin");
            user.setUserName("admin");
            user.setPhoneNumber("9876501234");
            user.setPassword(new BCryptPasswordEncoder().encode("admin")); // Encrypt password
            user.setUserRole(UserRole.ADMIN);
            userRepository.save(user);
            System.out.println("Admin account created successfully!");
        } else {
            System.out.println("Admin account already exists!");
        }
    }

    /**
     * Registers a new user with the CUSTOMER role.
     *
     * @param signupRequest the user registration data
     * @return the created user's DTO
     */
    @Override
    public UserDto signupUser(SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setUserName(signupRequest.getUserName());
        user.setFullName(signupRequest.getFullName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword())); // Encrypt password
        user.setUserRole(UserRole.CUSTOMER);

        User createdUser = userRepository.save(user);
        return createdUser.getUserDto(); // Convert to DTO before returning
    }

    /**
     * Registers a new user with the AGENT role and assigns a department.
     *
     * @param signupRequest the agent registration data
     * @return the created agent's DTO
     */
    @Override
    public UserDto signupAgent(SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setUserName(signupRequest.getUserName());
        user.setFullName(signupRequest.getFullName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword())); // Encrypt password
        user.setUserRole(UserRole.AGENT);

        // Set department if provided and valid
        if (UserRole.AGENT.equals(user.getUserRole()) && signupRequest.getDepartmentName() != null) {
            Optional<Department> department = departmentRepository.findByName(signupRequest.getDepartmentName());
            if (department.isPresent()) {
                user.setDepartment(department.get());
            } else {
                throw new RuntimeException("Department not found with name: " + signupRequest.getDepartmentName());
            }
        }

        User createdUser = userRepository.save(user);
        return createdUser.getUserDto(); // Convert to DTO before returning
    }

    /**
     * Checks if a user exists with the given username.
     *
     * @param username the username to check
     * @return true if the user exists, false otherwise
     */
    @Override
    public boolean hasUserWithUsername(String username) {
        return userRepository.findByUserName(username).isPresent();
    }

    /**
     * Checks if a user exists with the given email.
     *
     * @param email the email to check
     * @return true if the user exists, false otherwise
     */
    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
