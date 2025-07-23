package com.helpdesk.services.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.DepartmentDto;
import com.helpdesk.entities.Department;
import com.helpdesk.repositories.DepartmentRepository;

import lombok.RequiredArgsConstructor;

import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    // Injecting the department repository dependency
    private final DepartmentRepository departmentRepository;

    /**
     * Seeds the department table with default departments if the table is empty.
     * This method runs automatically after bean construction.
     */
    @PostConstruct
    public void seedDepartments() {
        if (departmentRepository.count() == 0) {
            departmentRepository.save(new Department("Technical"));
            departmentRepository.save(new Department("Finance"));
            departmentRepository.save(new Department("Marketing"));
            departmentRepository.save(new Department("Others"));
        }
    }

    /**
     * Fetches all departments from the database.
     * 
     * @return List of DepartmentDto
     */
    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new department based on provided DepartmentDto.
     * 
     * @param departmentDto DTO containing department data
     * @return Created DepartmentDto
     */
    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setName(departmentDto.getName());
        Department savedDepartment = departmentRepository.save(department);
        return convertToDto(savedDepartment);
    }

    /**
     * Updates an existing department by ID.
     * 
     * @param id Department ID to update
     * @param departmentDto Updated department data
     * @return Updated DepartmentDto or null if not found
     */
    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();
            department.setName(departmentDto.getName());
            Department updatedDepartment = departmentRepository.save(department);
            return convertToDto(updatedDepartment);
        }
        return null;
    }

    /**
     * Deletes a department by its ID.
     * 
     * @param id Department ID to delete
     */
    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    /**
     * Retrieves a department by its ID.
     * 
     * @param id Department ID
     * @return Corresponding DepartmentDto or null if not found
     */
    @Override
    public DepartmentDto getDepartmentById(Long id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        return optionalDepartment.map(this::convertToDto).orElse(null);
    }

    /**
     * Converts a Department entity to DepartmentDto.
     * 
     * @param department Department entity
     * @return Converted DepartmentDto
     */
    private DepartmentDto convertToDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }
}
