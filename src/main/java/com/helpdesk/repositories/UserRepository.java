package com.helpdesk.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpdesk.entities.User;
import com.helpdesk.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by their username (used for login/authentication)
    Optional<User> findByUserName(String username);

    // Find the first user with the given email address (used for signup or duplicate check)
    Optional<User> findFirstByEmail(String email);

    // Find a user by their role (e.g., ADMIN, AGENT, CUSTOMER)
    Optional<User> findByUserRole(UserRole userRole);

    // Find users with a specific role whose username contains a given substring (for filtering/search)
    List<User> findByUserRoleAndUserNameContaining(UserRole userRole, String username);

}
