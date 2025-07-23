package com.helpdesk.services.customer;

import java.util.List;

import com.helpdesk.dto.DepartmentDto;
import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

public interface CustomerService {

    /**
     * Allows a customer to create a new ticket.
     *
     * @param ticketDto the ticket data provided by the customer
     * @return the created TicketDto
     */
    TicketDto createTicket(TicketDto ticketDto);

    /**
     * Retrieves all tickets created by the currently logged-in customer.
     *
     * @return a list of TicketDto created by the customer
     */
    List<TicketDto> getAllTicketsCreated();

    /**
     * Deletes a specific ticket created by the customer.
     *
     * @param id the ID of the ticket to be deleted
     */
    void deleteTicket(Long id);

    /**
     * Allows a customer to update the status of their own ticket.
     *
     * @param ticketId  the ID of the ticket
     * @param newStatus the new status to be set
     * @return the updated TicketDto
     */
    TicketDto updateTicketStatus(Long ticketId, Status newStatus);

    /**
     * Searches for tickets created by the customer using a keyword in the title.
     *
     * @param title the keyword to search in ticket titles
     * @return a list of matching TicketDto
     */
    List<TicketDto> searchTicketByTitle(String title);

    /**
     * Retrieves a ticket by its ID, ensuring it belongs to the current customer.
     *
     * @param id the ID of the ticket
     * @return the corresponding TicketDto
     */
    TicketDto getTicketById(Long id);

    /**
     * Fetches all departments available in the system.
     *
     * @return a list of DepartmentDto
     */
    List<DepartmentDto> getAllDepartments();

    /**
     * Filters the customer's tickets by their priority level.
     *
     * @param priority the priority level to filter by
     * @return a list of TicketDto with the specified priority
     */
    List<TicketDto> filterTicketsByPriority(Priority priority);

    /**
     * Filters the customer's tickets by their current status.
     *
     * @param status the ticket status to filter by
     * @return a list of TicketDto with the specified status
     */
    List<TicketDto> filterTicketsByStatus(Status status);

    /**
     * Filters the customer's tickets by the department name they are assigned to.
     *
     * @param name the department name
     * @return a list of TicketDto under the specified department
     */
    List<TicketDto> filterTicketsByDepartmentName(String name);
}
