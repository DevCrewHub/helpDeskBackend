package com.helpdesk.services.admin;

import java.util.List;

import com.helpdesk.dto.DepartmentDto;

/**
 * DepartmentService interface defines operations related to department management.
 */
public interface DepartmentService {

    /**
     * Retrieves all departments.
     * 
     * @return List of DepartmentDto representing all departments
     */
    List<DepartmentDto> getAllDepartments();

    /**
     * Creates a new department.
     * 
     * @param departmentDto Data of the department to create
     * @return Created DepartmentDto
     */
    DepartmentDto createDepartment(DepartmentDto departmentDto);

    /**
     * Updates an existing department by ID.
     * 
     * @param id ID of the department to update
     * @param departmentDto Updated department data
     * @return Updated DepartmentDto
     */
    DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto);

    /**
     * Deletes a department by its ID.
     * 
     * @param id ID of the department to delete
     */
    void deleteDepartment(Long id);

    /**
     * Retrieves a department by its ID.
     * 
     * @param id ID of the department
     * @return DepartmentDto corresponding to the ID
     */
    DepartmentDto getDepartmentById(Long id);
}
