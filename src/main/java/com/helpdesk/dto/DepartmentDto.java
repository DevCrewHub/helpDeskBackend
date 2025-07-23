package com.helpdesk.dto;

import lombok.Data;

/**
 * Data Transfer Object for Department entity.
 * Used to transfer department data between client and server.
 */
@Data  // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods
public class DepartmentDto {

    // Unique identifier for the department
    private Long id;

    // Name of the department (e.g., Technical, Finance, etc.)
    private String name;
}
