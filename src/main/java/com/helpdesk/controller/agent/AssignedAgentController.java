package com.helpdesk.controller.agent;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;
import com.helpdesk.services.agent.AgentService;

import lombok.RequiredArgsConstructor;

/**
 * Controller for handling tickets assigned to the currently authenticated agent.
 */
@RestController
@RequestMapping("/api/agent/assigned")
@RequiredArgsConstructor
public class AssignedAgentController {

    // Service to perform operations on tickets assigned to an agent
    private final AgentService agentService;

    /**
     * Get all tickets assigned to the current agent.
     */
    @GetMapping("/tickets")
    public ResponseEntity<?> getAssignedTickets() {
        return ResponseEntity.ok(agentService.getAssignedTickets());
    }

    /**
     * Get a specific assigned ticket by its ID.
     */
    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketDto> getAssignedTicketById(@PathVariable Long id) {
        TicketDto ticket = agentService.getAssignedTicketById(id);
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(ticket);
    }

    /**
     * Search assigned tickets by title.
     */
    @GetMapping("/tickets/search/{title}")
    public ResponseEntity<List<TicketDto>> searchAssignedTicketByTitle(@PathVariable String title) {
        return ResponseEntity.ok(agentService.searchAssignedTicketByTitle(title));
    }

    /**
     * Filter assigned tickets by priority (e.g., HIGH, MEDIUM, LOW).
     */
    @GetMapping("/tickets/priority/{priority}")
    public ResponseEntity<List<TicketDto>> filterAssignedTicketsByPriority(@PathVariable String priority) {
        try {
            Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
            List<TicketDto> tickets = agentService.filterAssignedTicketsByPriority(priorityEnum);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            // If the priority is invalid, return 400 BAD REQUEST
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Filter assigned tickets by status (e.g., OPEN, CLOSED).
     */
    @GetMapping("/tickets/status/{status}")
    public ResponseEntity<List<TicketDto>> filterAssignedTicketsByStatus(@PathVariable String status) {
        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            List<TicketDto> tickets = agentService.filterAssignedTicketsByStatus(statusEnum);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            // If the status is invalid, return 400 BAD REQUEST
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Filter assigned tickets by department name.
     */
    @GetMapping("/tickets/department/{name}")
    public ResponseEntity<List<TicketDto>> filterAssignedTicketsByDepartmentName(@PathVariable String name) {
        List<TicketDto> tickets = agentService.filterAssignedTicketsByDepartmentName(name);
        return ResponseEntity.ok(tickets);
    }

    /**
     * Update the priority of a specific assigned ticket.
     */
    @PutMapping("/tickets/{ticketId}/priority")
    public ResponseEntity<?> updatAssignedTicketePriority(@PathVariable Long ticketId, @RequestParam String priority) {
        try {
            Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
            TicketDto updatedTicket = agentService.updatAssignedTicketePriority(ticketId, priorityEnum);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            // Invalid priority input
            return ResponseEntity.badRequest().body("Invalid priority: " + priority);
        } catch (RuntimeException e) {
            // Handle service errors (e.g., ticket not found)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Update the status of a specific assigned ticket.
     */
    @PutMapping("/tickets/{ticketId}/status")
    public ResponseEntity<?> updateAssignedTicketStatus(@PathVariable Long ticketId, @RequestParam String status) {
        try {
            Status newStatus = Status.valueOf(status.toUpperCase());
            TicketDto updatedTicket = agentService.updateAssignedTicketStatus(ticketId, newStatus);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            // Invalid status input
            return ResponseEntity.badRequest().body("Invalid status: " + status);
        } catch (RuntimeException e) {
            // Handle service errors
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get the list of all departments.
     */
    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments() {
        return ResponseEntity.ok(agentService.getAllDepartments());
    }

}
