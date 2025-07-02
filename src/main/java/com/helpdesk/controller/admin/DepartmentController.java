package com.helpdesk.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.helpdesk.dto.DepartmentDto;
import com.helpdesk.services.admin.DepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/department")
public class DepartmentController {

	private final DepartmentService departmentService;

	@GetMapping
	public ResponseEntity<?> getAllDepartments() {
		return ResponseEntity.ok(departmentService.getAllDepartments());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getDepartmentById(@PathVariable Long id) {
		DepartmentDto department = departmentService.getDepartmentById(id);
		if (department != null) {
			return ResponseEntity.ok(department);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> createDepartment(@RequestBody DepartmentDto departmentDto) {
		DepartmentDto createdDepartment = departmentService.createDepartment(departmentDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto departmentDto) {
		DepartmentDto updatedDepartment = departmentService.updateDepartment(id, departmentDto);
		if (updatedDepartment != null) {
			return ResponseEntity.ok(updatedDepartment);
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
		departmentService.deleteDepartment(id);
		return ResponseEntity.ok().build();
	}
}