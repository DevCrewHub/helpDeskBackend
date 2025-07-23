package com.helpdesk.dto;

import java.util.Date;
import java.util.List;

import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

import lombok.Data;

@Data // Lombok annotation to generate boilerplate code (getters, setters, etc.)
public class TicketDto {

    // Unique identifier for the ticket
    private Long id;

    // Title of the ticket
    private String title;

    // Detailed description of the issue
    private String description;
    
    // Timestamp when the ticket was created (auto-set)
    private Date createdDate;

    // Priority level of the ticket (e.g., LOW, MEDIUM, HIGH)
    private Priority priority;

    // Current status of the ticket (e.g., PENDING, INPROGRESS, RESOLVED)
    private Status status;

    // ID of the customer who created the ticket (auto-set from logged-in user)
    private Long customerId;

    // Name of the customer who created the ticket (auto-set from logged-in user)
    private String customerName;

    // ID of the agent assigned to this ticket
    private Long agentId;

    // Name of the agent assigned to this ticket
    private String agentName;

    // ID of the department the ticket is associated with
    private Long departmentId;

    // Name of the department the ticket is associated with
    private String departmentName;

    // List of comments associated with this ticket
    private List<CommentDto> comments;
}
