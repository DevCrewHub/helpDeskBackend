package com.helpdesk.entities;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.helpdesk.dto.UserDto;
import com.helpdesk.enums.UserRole;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data // Lombok annotation to generate boilerplate code (getters, setters, toString, etc.)
@Entity // Marks this class as a JPA entity
public class User implements UserDetails { // Implements Spring Security's UserDetails for authentication

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates primary key
	private Long id;

	@Column(name="user_name", unique = true, nullable = false) // Unique username required
	private String userName;

	@NotBlank(message = "Email is required") // Ensures email is not blank
	@Email(message = "Invalid email format") // Ensures valid email format
	@Column(name = "email", nullable = false, unique = true) // Email must be unique and not null
	private String email;

	@Column(nullable = false) // Password is mandatory
	private String password;

	@Column(name = "full_name", nullable = false) // Full name is required
	private String fullName;

	@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number") // Validates phone number pattern
	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	@Column(name = "user_role", nullable = false) // Role like ADMIN or EMPLOYEE
	private UserRole userRole;

	@ManyToOne // Many users can belong to one department
	@JoinColumn(name = "department_id") // Foreign key to department table
	private Department department;

	// Returns user authorities (used for role-based access in Spring Security)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + this.userRole.name()));
	}

	// Indicates whether the user's account has expired
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// Indicates whether the user is locked or unlocked
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// Indicates whether the user's credentials (password) has expired
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// Indicates whether the user is enabled or disabled
	@Override
	public boolean isEnabled() {
		return true;
	}

	// Returns the username (used for login)
	@Override
	public String getUsername() {
		return this.userName;
	}

	// Converts the User entity to a UserDto for API usage
	public UserDto getUserDto() {
		UserDto userDto = new UserDto();
		userDto.setId(id);
		userDto.setUserName(userName);
		userDto.setEmail(email);
		userDto.setFullName(fullName);
		userDto.setPhoneNumber(phoneNumber);
		userDto.setUserRole(userRole);
		if (department != null) {
			userDto.setDepartmentId(department.getId());
			userDto.setDepartmentName(department.getName());
		}
		return userDto;
	}
}
