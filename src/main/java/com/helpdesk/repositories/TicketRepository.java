package com.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // ---------- Queries for Customers ----------

    // Find all tickets created by a specific customer
    List<Ticket> findByCustomer(User customer);

    // Search tickets by title containing a keyword for a specific customer
    List<Ticket> findByCustomerAndTitleContaining(User customer, String title);

    // Find a specific ticket by customer and ticket ID
    Ticket findTicketByCustomerAndId(User assignedAgent, Long id);

    // Filter tickets by priority for a specific customer
    List<Ticket> findByCustomerAndPriority(User customer, Priority priority);

    // Filter tickets by status for a specific customer
    List<Ticket> findByCustomerAndStatus(User customer, Status status);

    // Filter tickets by department name for a specific customer
    List<Ticket> findByCustomerAndDepartmentName(User customer, String name);


    // ---------- Queries for Admin ----------

    // Search all tickets by title containing a keyword
    List<Ticket> findAllByTitleContaining(String title);

    // Get all tickets with a specific priority
    List<Ticket> findByPriority(Priority priority);

    // Get all tickets with a specific status
    List<Ticket> findByStatus(Status status);

    // Get all tickets by department name
    List<Ticket> findByDepartmentName(String name);


    // ---------- Queries for Agents ----------

    // Find all tickets assigned to a specific agent
    List<Ticket> findByAssignedAgent(User assignedAgent);

    // Find a specific ticket by agent and ticket ID
    Ticket findTicketByAssignedAgentAndId(User assignedAgent, Long id);

    // Search tickets by title containing a keyword for a specific agent
    List<Ticket> findByAssignedAgentAndTitleContaining(User assignedAgent, String title);

    // Filter tickets by priority for a specific agent
    List<Ticket> findByAssignedAgentAndPriority(User assignedAgentr, Priority priority);

    // Filter tickets by status for a specific agent
    List<Ticket> findByAssignedAgentAndStatus(User assignedAgentr, Status status);

    // Filter tickets by department name containing a keyword for a specific agent
    List<Ticket> findByAssignedAgentAndDepartment_NameContaining(User agent, String departmentName);

}
