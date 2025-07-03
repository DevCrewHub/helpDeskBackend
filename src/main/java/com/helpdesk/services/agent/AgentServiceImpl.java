package com.helpdesk.services.agent;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;
import com.helpdesk.repositories.TicketRepository;
import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final TicketRepository ticketRepository;
    private final JwtUtil jwtUtil;

    @Override
    public List<TicketDto> getAssignedTickets() {
        User agent = jwtUtil.getLoggedInUser();
        if (agent != null) {
            return ticketRepository.findByAssignedAgent(agent)
                    .stream()
                    .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                    .map(Ticket::getTicketDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
    
    @Override
	public TicketDto getAssignedTicketById(Long id) {
    	User agent = jwtUtil.getLoggedInUser();
		return Optional.ofNullable(ticketRepository.findTicketByAssignedAgentAndId(agent, id))
		    .map(Ticket::getTicketDto)
		    .orElse(null);
	}
    
    @Override
    public List<TicketDto> searchAssignedTicketByTitle(String title) {
        User agent = jwtUtil.getLoggedInUser();
        if (agent != null) {
            return ticketRepository.findByAssignedAgentAndTitleContaining(agent, title)
                    .stream()
                    .map(Ticket::getTicketDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
    
    @Override
	public List<TicketDto> filterAssignedTicketsByPriority(Priority priority) {
    	User agent = jwtUtil.getLoggedInUser();
        if (agent != null) {
        	return ticketRepository.findByAssignedAgentAndPriority(agent, priority)
    				.stream()
    				.sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
    				.map(Ticket::getTicketDto)
    				.collect(Collectors.toList());
        }
        return List.of();
	}
    
    @Override
   	public List<TicketDto> filterAssignedTicketsByStatus(Status status) {
       	User agent = jwtUtil.getLoggedInUser();
           if (agent != null) {
           	return ticketRepository.findByAssignedAgentAndStatus(agent, status)
       				.stream()
       				.sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
       				.map(Ticket::getTicketDto)
       				.collect(Collectors.toList());
           }
           return List.of();
   	}
    
    @Override
    public List<TicketDto> filterAssignedTicketsByDepartmentName(String name){
    	User agent = jwtUtil.getLoggedInUser();
        if (agent != null) {
            return ticketRepository.findByAssignedAgentAndDepartment_NameContaining(agent, name)
                    .stream()
                    .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                    .map(Ticket::getTicketDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
    
    @Override
    public TicketDto updatAssignedTicketePriority(Long ticketId, Priority priority) {
    	User agent = jwtUtil.getLoggedInUser();
        if (agent == null) {
            throw new RuntimeException("Agent not authenticated");
        }
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        }
        Ticket ticket = optionalTicket.get();
        if (!agent.equals(ticket.getAssignedAgent())) {
            throw new RuntimeException("You can only update tickets assigned to you");
        }
        ticket.setPriority(priority);
        return ticketRepository.save(ticket).getTicketDto();
    }
    
    @Override
    public TicketDto updateAssignedTicketStatus(Long ticketId, Status newStatus) {
        User agent = jwtUtil.getLoggedInUser();
        if (agent == null) {
            throw new RuntimeException("Agent not authenticated");
        }        
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        }        
        Ticket ticket = optionalTicket.get();
        if (!agent.equals(ticket.getAssignedAgent())) {
            throw new RuntimeException("You can only update tickets assigned to you");
        }
        if (!isValidStatusTransition(ticket.getStatus(), newStatus)) {
            throw new RuntimeException("Invalid status transition from " + ticket.getStatus() + " to " + newStatus);
        }
        ticket.setStatus(newStatus);
        return ticketRepository.save(ticket).getTicketDto();
    }    
    private boolean isValidStatusTransition(Status currentStatus, Status newStatus) {
        if (currentStatus == Status.INPROGRESS && newStatus == Status.RESOLVED) {
            return true;
        }
        return false;
    }
    
}
