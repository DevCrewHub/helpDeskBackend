package com.helpdesk.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data // Lombok annotation to generate boilerplate code like getters/setters
@Entity // Marks this class as a JPA entity
public class Department {

	@Id // Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID generation
	private Long id;

	@NotBlank(message = "Department name is required") // Ensures name is not null or empty
	@Column(name = "name", nullable = false, unique = true) // DB column with non-null & unique constraint
	private String name;

	@OneToMany(mappedBy = "department") // One department can have many users (employees or agents)
	private List<User> users;

	// Default constructor (required by JPA)
	public Department() {}

	// Parameterized constructor to initialize with a department name
	public Department(String name) {
		this.name = name;
	}
}
