package com.helpdesk.controller.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Status;
import com.helpdesk.services.customer.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping("/ticket")
	public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticketDto) {
		TicketDto createTicketDto = customerService.createTicket(ticketDto);
		if (createTicketDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();		}

		return ResponseEntity.status(HttpStatus.CREATED).body(createTicketDto);
	}
	
	@GetMapping("/ticketsCreated")
    public ResponseEntity<?> getAllTicketsCreated() {
		return ResponseEntity.ok(customerService.getAllTicketsCreated());
    }
	
	@DeleteMapping("/ticket/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
		customerService.deleteTicket(id);
        return ResponseEntity.ok(null);
    }
    
    @GetMapping("/tickets/search/{title}")
    public ResponseEntity<?> searchTicketsByTitle(@PathVariable String title) {
        return ResponseEntity.ok(customerService.searchTicketByTitle(title));
    }
    
    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
    	TicketDto ticket = customerService.getTicketById(id);
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(ticket);
    }
    
    @PutMapping("/tickets/{ticketId}/status")
    public ResponseEntity<?> updateTicketStatus(@PathVariable Long ticketId, @RequestParam String status) {
        try {
            Status newStatus = Status.valueOf(status.toUpperCase());
            TicketDto updatedTicket = customerService.updateTicketStatus(ticketId, newStatus);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + status);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments() {
        return ResponseEntity.ok(customerService.getAllDepartments());
    }

}