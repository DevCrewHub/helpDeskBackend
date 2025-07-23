package com.helpdesk.services.admin;

import java.util.List;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.dto.UserDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

/**
 * AdminService defines operations available to administrators
 * for managing users and tickets in the HelpDesk system.
 */
public interface AdminService {

    /**
     * Retrieve a list of all registered customers.
     */
    List<UserDto> getCustomers();

    /**
     * Retrieve a list of all registered agents.
     */
    List<UserDto> getAgents();

    /**
     * Search for customers by username.
     */
    List<UserDto> searchCustomersByUsername(String username);

    /**
     * Search for agents by username.
     */
    List<UserDto> searchAgentsByUsername(String username);

    /**
     * Retrieve a list of all tickets in the system.
     */
    List<TicketDto> getAllTickets();

    /**
     * Get the details of a specific ticket by its ID.
     */
    TicketDto getTicketById(Long id);

    /**
     * Assign a ticket to an agent.
     *
     * @param ticketId ID of the ticket to assign
     * @param agentId ID of the agent to assign the ticket to
     * @return Updated ticket with assigned agent
     */
    TicketDto assignTicket(Long ticketId, Long agentId);

    /**
     * Search tickets by their title.
     */
    List<TicketDto> searchTicketByTitle(String title);

    /**
     * Filter tickets by priority level.
     */
    List<TicketDto> filterTicketsByPriority(Priority priority);

    /**
     * Filter tickets by current status.
     */
    List<TicketDto> filterTicketsByStatus(Status status);

    /**
     * Filter tickets by the name of the department.
     */
    List<TicketDto> filterTicketsByDepartmentName(String name);

    /**
     * Delete a customer by their ID.
     */
    void deleteCustomer(Long customerId);

    /**
     * Delete an agent by their ID.
     */
    void deleteAgent(Long agentId);
}
