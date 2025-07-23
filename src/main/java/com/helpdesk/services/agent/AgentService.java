package com.helpdesk.services.agent;

import java.util.List;

import com.helpdesk.dto.DepartmentDto;
import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

/**
 * Service interface for handling ticket operations specific to agent users.
 * Includes methods to fetch, update, filter, and assign tickets.
 */
public interface AgentService {

    // Returns the list of tickets currently assigned to the logged-in agent
    List<TicketDto> getAssignedTickets();

    // Retrieves a specific assigned ticket by its ID
    TicketDto getAssignedTicketById(Long id);

    // Searches assigned tickets by matching title
    List<TicketDto> searchAssignedTicketByTitle(String title);

    // Filters assigned tickets based on their priority
    List<TicketDto> filterAssignedTicketsByPriority(Priority priority);

    // Filters assigned tickets based on their status (e.g., INPROGRESS, RESOLVED)
    List<TicketDto> filterAssignedTicketsByStatus(Status status);

    // Filters assigned tickets by department name
    List<TicketDto> filterAssignedTicketsByDepartmentName(String name);

    // Updates the priority of an assigned ticket
    TicketDto updatAssignedTicketePriority(Long ticketId, Priority priority);

    // Updates the status of an assigned ticket
    TicketDto updateAssignedTicketStatus(Long ticketId, Status newStatus);

    // Returns a list of all tickets in the system (admin-level access for agents, if allowed)
    List<TicketDto> getAllTickets();

    // Retrieves any ticket by its ID (not limited to assigned ones)
    TicketDto getTicketById(Long id);

    // Searches all tickets by title
    List<TicketDto> searchTicketByTitle(String title);

    // Filters all tickets based on priority
    List<TicketDto> filterTicketsByPriority(Priority priority);

    // Filters all tickets based on status
    List<TicketDto> filterTicketsByStatus(Status status);

    // Filters all tickets by department name
    List<TicketDto> filterTicketsByDepartmentName(String name);

    // Assigns a specific ticket to the currently logged-in agent
    TicketDto assignTicketToMe(Long ticketId);

    // Retrieves a list of all departments in the system
    List<DepartmentDto> getAllDepartments();
}
