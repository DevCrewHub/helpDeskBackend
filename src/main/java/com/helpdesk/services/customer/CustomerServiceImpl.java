package com.helpdesk.services.customer;

import java.time.LocalDate;
import java.util.Comparator;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.entities.Department;
import com.helpdesk.enums.Status;
import com.helpdesk.repositories.TicketRepository;
import com.helpdesk.repositories.DepartmentRepository;
import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    // private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final JwtUtil jwtUtil;
    private final DepartmentRepository departmentRepository;

    
    // create ticket
    @Override
    public TicketDto createTicket(TicketDto ticketDto) {
        User loggedInUser = jwtUtil.getLoggedInUser();

        if (loggedInUser != null && ticketDto.getDepartmentName() != null) {
            Department department = departmentRepository.findByName(ticketDto.getDepartmentName()).orElse(null);
            if (department == null) {
                throw new RuntimeException("Department not found");
            }
            Ticket ticket = new Ticket();
            ticket.setTitle(ticketDto.getTitle());
            ticket.setDescription(ticketDto.getDescription());
            ticket.setPriority(ticketDto.getPriority());
//            ticket.setCreatedDate(new Date());
            ticket.setCreatedDate(Date.valueOf(LocalDate.now()));
            ticket.setStatus(Status.PENDING); // Start with PENDING status
            ticket.setCustomer(loggedInUser); // Set the logged-in user as customer
            ticket.setDepartment(department);
            return ticketRepository.save(ticket).getTicketDto();
        }
        return null;
    }
    
    // get all tickets created by the user
    @Override
    public List<TicketDto> getAllTicketsCreated() {
        User loggedInUser = jwtUtil.getLoggedInUser();
        if (loggedInUser == null) {
            return List.of(); // or throw an exception
        }
        List<Ticket> tickets = ticketRepository.findByCustomer(loggedInUser);
        return tickets
        		.stream()
                .sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
                .map(Ticket::getTicketDto)
                .collect(Collectors.toList());
    }
    
    // delete ticket
    @Override
	public void deleteTicket(Long id) {
    	ticketRepository.deleteById(id);
	}
    
    // search tickets by title
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
    
    //search ticket by id
    @Override
	public TicketDto getTicketById(Long id) {
    	User customer = jwtUtil.getLoggedInUser();
		return Optional.ofNullable(ticketRepository.findTicketByCustomerAndId(customer, id))
		    .map(Ticket::getTicketDto)
		    .orElse(null);
	}
    
    // change status to closed
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
        if (!customer.equals(ticket.getCustomer())) {
            throw new RuntimeException("You can only update tickets created by you");
        }
        if (ticket.getStatus() == Status.CLOSED) {
            throw new RuntimeException("Cannot update a closed ticket.");
        }
        if (newStatus != Status.CLOSED) {
            throw new RuntimeException("You can only change the status to CLOSED.");
        }
        ticket.setStatus(Status.CLOSED);
        return ticketRepository.save(ticket).getTicketDto();
    }

}
