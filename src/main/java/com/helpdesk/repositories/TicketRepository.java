package com.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpdesk.entities.Department;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

//    List<Ticket> findByCustomer(User customer);
//    
//    List<Ticket> findByTicketStatus(TicketStatus ticketStatus);
//
//    List<Ticket> findByAssignedAgent(User assignedAgent);
//    
//    List<Ticket> findAllByTitleContaining(String title);
//
//    List<Ticket> findByAssignedAgentAndTitleContaining(User assignedAgent, String title);
//    
//    List<Ticket> findByCustomerAndTitleContaining(User customer, String title);
//    
//    Ticket findTicketByAssignedAgentAndId(User assignedAgent, Long id);
//    
//    Ticket findTicketByCustomerAndId(User assignedAgent, Long id);
//    
//    List<Ticket> findByPriority(Priority priority);
//    
//    List<Ticket> findByDepartmentName(String name);
//    
//    List<Ticket> findByAssignedAgentAndPriority(User assignedAgentr, Priority priority);
//    
//    List<Ticket> findByAssignedAgentAndTicketStatus(User assignedAgentr, TicketStatus ticketStatus);

}
