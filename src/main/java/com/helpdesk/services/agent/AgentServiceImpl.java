package com.helpdesk.services.agent;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.DepartmentDto;
import com.helpdesk.dto.TicketDto;
import com.helpdesk.entities.Department;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;
import com.helpdesk.repositories.DepartmentRepository;
import com.helpdesk.repositories.TicketRepository;
import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for agent-related ticket operations.
 * Agents can view, filter, search, and update tickets assigned to them.
 */
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final TicketRepository ticketRepository;
    private final JwtUtil jwtUtil;
    private final DepartmentRepository departmentRepository;

    // Retrieves all tickets assigned to the currently logged-in agent
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

    // Gets a specific ticket assigned to the current agent by ID
    @Override
    public TicketDto getAssignedTicketById(Long id) {
        User agent = jwtUtil.getLoggedInUser();
        return Optional.ofNullable(ticketRepository.findTicketByAssignedAgentAndId(agent, id))
                .map(Ticket::getTicketDto)
                .orElse(null);
    }

    // Searches assigned tickets by title
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

    // Filters assigned tickets by priority
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

    // Filters assigned tickets by status
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

    // Filters assigned tickets by department name
    @Override
    public List<TicketDto> filterAssignedTicketsByDepartmentName(String name) {
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

    // Updates the priority of an assigned ticket
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

    // Updates the status of an assigned ticket after validating transition
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

    // Validates allowed status transitions (e.g., INPROGRESS → RESOLVED)
    private boolean isValidStatusTransition(Status currentStatus, Status newStatus) {
        return currentStatus == Status.INPROGRESS && newStatus == Status.RESOLVED;
    }

    // Returns all tickets in the system (admin-like access for agents)
    @Override
    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                .map(Ticket::getTicketDto)
                .collect(Collectors.toList());
    }

    // Gets any ticket by ID
    @Override
    public TicketDto getTicketById(Long id) {
        return ticketRepository.findById(id)
                .map(Ticket::getTicketDto)
                .orElse(null);
    }

    // Searches all tickets by title
    @Override
    public List<TicketDto> searchTicketByTitle(String title) {
        return ticketRepository.findAllByTitleContaining(title)
                .stream()
                .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                .map(Ticket::getTicketDto)
                .collect(Collectors.toList());
    }

    // Filters all tickets by priority
    @Override
    public List<TicketDto> filterTicketsByPriority(Priority priority) {
        return ticketRepository.findByPriority(priority)
                .stream()
                .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                .map(Ticket::getTicketDto)
                .collect(Collectors.toList());
    }

    // Filters all tickets by status
    @Override
    public List<TicketDto> filterTicketsByStatus(Status status) {
        return ticketRepository.findByStatus(status)
                .stream()
                .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                .map(Ticket::getTicketDto)
                .collect(Collectors.toList());
    }

    // Filters all tickets by department name
    @Override
    public List<TicketDto> filterTicketsByDepartmentName(String name) {
        return ticketRepository.findByDepartmentName(name)
                .stream()
                .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                .map(Ticket::getTicketDto)
                .collect(Collectors.toList());
    }

    // Converts Department entity to DTO
    private DepartmentDto convertToDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }

    // Retrieves all departments
    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Assigns a ticket to the current agent if it's unassigned
    @Override
    public TicketDto assignTicketToMe(Long ticketId) {
        User agent = jwtUtil.getLoggedInUser();
        if (agent == null) {
            throw new RuntimeException("Agent not authenticated");
        }

        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        }

        Ticket ticket = optionalTicket.get();
        if (ticket.getAssignedAgent() != null) {
            throw new RuntimeException("Ticket is already assigned to another agent");
        }

        ticket.setAssignedAgent(agent);
        ticket.setStatus(Status.INPROGRESS);
        return ticketRepository.save(ticket).getTicketDto();
    }
}
