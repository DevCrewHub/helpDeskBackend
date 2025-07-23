package com.helpdesk.services.customer;

import java.time.LocalDate;
import java.util.Comparator;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.DepartmentDto;
import com.helpdesk.dto.TicketDto;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.entities.Department;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;
import com.helpdesk.repositories.TicketRepository;
import com.helpdesk.repositories.DepartmentRepository;
import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    // Dependencies required for ticket and department handling
    private final TicketRepository ticketRepository;
    private final JwtUtil jwtUtil;
    private final DepartmentRepository departmentRepository;

    // Create a new ticket for the currently logged-in user
    @Override
    public TicketDto createTicket(TicketDto ticketDto) {
        User loggedInUser = jwtUtil.getLoggedInUser();

        if (loggedInUser != null && ticketDto.getDepartmentName() != null) {
            // Fetch department by name
            Department department = departmentRepository.findByName(ticketDto.getDepartmentName()).orElse(null);
            if (department == null) {
                throw new RuntimeException("Department not found");
            }

            // Create and populate the ticket entity
            Ticket ticket = new Ticket();
            ticket.setTitle(ticketDto.getTitle());
            ticket.setDescription(ticketDto.getDescription());
            ticket.setPriority(ticketDto.getPriority());
            ticket.setCreatedDate(Date.valueOf(LocalDate.now())); // Set current date
            ticket.setStatus(Status.PENDING); // Default status
            ticket.setCustomer(loggedInUser); // Assign customer
            ticket.setDepartment(department);

            return ticketRepository.save(ticket).getTicketDto();
        }
        return null;
    }

    // Retrieve all tickets created by the currently logged-in user
    @Override
    public List<TicketDto> getAllTicketsCreated() {
        User loggedInUser = jwtUtil.getLoggedInUser();
        if (loggedInUser == null) {
            return List.of(); // Return empty list if user is not authenticated
        }

        // Fetch and sort tickets by creation date (descending)
        List<Ticket> tickets = ticketRepository.findByCustomer(loggedInUser);
        return tickets.stream()
                .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                .map(Ticket::getTicketDto)
                .collect(Collectors.toList());
    }

    // Delete a ticket by its ID
    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    // Search tickets by title for the currently logged-in user
    @Override
    public List<TicketDto> searchTicketByTitle(String title) {
        User customer = jwtUtil.getLoggedInUser();
        if (customer != null) {
            return ticketRepository.findByCustomerAndTitleContaining(customer, title)
                    .stream()
                    .map(Ticket::getTicketDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    // Retrieve a single ticket by ID (must belong to logged-in user)
    @Override
    public TicketDto getTicketById(Long id) {
        User customer = jwtUtil.getLoggedInUser();
        return Optional.ofNullable(ticketRepository.findTicketByCustomerAndId(customer, id))
                .map(Ticket::getTicketDto)
                .orElse(null);
    }

    // Update the status of a ticket to CLOSED (only by the ticket's creator)
    @Override
    public TicketDto updateTicketStatus(Long ticketId, Status newStatus) {
        User customer = jwtUtil.getLoggedInUser();
        if (customer == null) {
            throw new RuntimeException("Customer not authenticated");
        }

        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        }

        Ticket ticket = optionalTicket.get();

        // Only the ticket owner can update the status
        if (!customer.equals(ticket.getCustomer())) {
            throw new RuntimeException("You can only update tickets created by you");
        }

        // Prevent updates to already closed tickets
        if (ticket.getStatus() == Status.CLOSED) {
            throw new RuntimeException("Cannot update a closed ticket.");
        }

        // Only allow transition to CLOSED status
        if (newStatus != Status.CLOSED) {
            throw new RuntimeException("You can only change the status to CLOSED.");
        }

        ticket.setStatus(Status.CLOSED);
        return ticketRepository.save(ticket).getTicketDto();
    }

    // Utility method to convert Department entity to DTO
    private DepartmentDto convertToDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }

    // Retrieve all departments available in the system
    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Filter tickets by priority for the current user
    @Override
    public List<TicketDto> filterTicketsByPriority(Priority priority) {
        User customer = jwtUtil.getLoggedInUser();
        if (customer != null) {
            return ticketRepository.findByCustomerAndPriority(customer, priority)
                    .stream()
                    .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                    .map(Ticket::getTicketDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    // Filter tickets by status for the current user
    @Override
    public List<TicketDto> filterTicketsByStatus(Status status) {
        User customer = jwtUtil.getLoggedInUser();
        if (customer != null) {
            return ticketRepository.findByCustomerAndStatus(customer, status)
                    .stream()
                    .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                    .map(Ticket::getTicketDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    // Filter tickets by department name for the current user
    @Override
    public List<TicketDto> filterTicketsByDepartmentName(String name) {
        User customer = jwtUtil.getLoggedInUser();
        if (customer != null) {
            return ticketRepository.findByCustomerAndDepartmentName(customer, name)
                    .stream()
                    .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                    .map(Ticket::getTicketDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
