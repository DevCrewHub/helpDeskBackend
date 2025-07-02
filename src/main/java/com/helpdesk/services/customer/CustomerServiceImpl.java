package com.helpdesk.services.customer;

import java.time.LocalDate;
import java.util.Comparator;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

//import java.util.Optional;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.entities.Department;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;
import com.helpdesk.repositories.TicketRepository;
import com.helpdesk.repositories.DepartmentRepository;
//import com.helpdesk.repositories.UserRepository;
import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    // private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final JwtUtil jwtUtil;
    private final DepartmentRepository departmentRepository;

    @Override
    public TicketDto createTicket(TicketDto ticketDto) {
        // Get the currently logged-in user (customer)
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

}
