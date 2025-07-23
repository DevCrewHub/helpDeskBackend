package com.helpdesk.controller.agent;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;
import com.helpdesk.services.agent.AgentService;

import lombok.RequiredArgsConstructor;

/**
 * Controller for agent-specific operations such as managing and viewing tickets.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agent")
public class AgentController {

	private final AgentService agentService; // Service layer for agent functionality

	/**
	 * Retrieves all tickets visible to the agent.
	 */
	@GetMapping("/tickets")
	public ResponseEntity<?> getAllTickets() {
		return ResponseEntity.ok(agentService.getAllTickets());
	}

	/**
	 * Retrieves a ticket by its ID.
	 */
	@GetMapping("/ticket/{id}")
	public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
		TicketDto ticket = agentService.getTicketById(id);
		if (ticket == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(ticket);
	}

	/**
	 * Searches for tickets by title.
	 */
	@GetMapping("/tickets/search/{title}")
	public ResponseEntity<List<TicketDto>> searchTicket(@PathVariable String title) {
		return ResponseEntity.ok(agentService.searchTicketByTitle(title));
	}

	/**
	 * Filters tickets based on priority (HIGH, MEDIUM, LOW).
	 */
	@GetMapping("/tickets/priority/{priority}")
	public ResponseEntity<List<TicketDto>> filterTicketsByPriority(@PathVariable String priority) {
		try {
			Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
			List<TicketDto> tickets = agentService.filterTicketsByPriority(priorityEnum);
			return ResponseEntity.ok(tickets);
		} catch (IllegalArgumentException e) {
			// Return 400 BAD REQUEST if invalid priority is passed
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Filters tickets based on status (e.g., OPEN, CLOSED, IN_PROGRESS).
	 */
	@GetMapping("/tickets/status/{status}")
	public ResponseEntity<List<TicketDto>> filterTicketsByStatus(@PathVariable String status) {
		try {
			Status statusEnum = Status.valueOf(status.toUpperCase());
			List<TicketDto> tickets = agentService.filterTicketsByStatus(statusEnum);
			return ResponseEntity.ok(tickets);
		} catch (IllegalArgumentException e) {
			// Return 400 BAD REQUEST if invalid status is passed
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Filters tickets by the department name.
	 */
	@GetMapping("/tickets/department/{name}")
	public ResponseEntity<List<TicketDto>> filterTicketsByDepartmentName(@PathVariable String name) {
		return ResponseEntity.ok(agentService.filterTicketsByDepartmentName(name));
	}

	/**
	 * Assigns the ticket with the given ID to the currently logged-in agent.
	 */
	@PutMapping("/tickets/{ticketId}/assign")
	public ResponseEntity<?> assignTicketToMe(@PathVariable Long ticketId) {
		try {
			TicketDto updatedTicket = agentService.assignTicketToMe(ticketId);
			return ResponseEntity.ok(updatedTicket);
		} catch (RuntimeException e) {
			// Handle failure (e.g., ticket not found or already assigned)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Retrieves all departments accessible to the agent.
	 */
	@GetMapping("/departments")
	public ResponseEntity<?> getAllDepartments() {
		return ResponseEntity.ok(agentService.getAllDepartments());
	}
}
