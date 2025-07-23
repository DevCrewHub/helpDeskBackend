package com.helpdesk.entities;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

import lombok.Data;

@Data // Lombok annotation to generate getters/setters, toString, equals, etc.
@Entity // Marks this class as a JPA entity
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated primary key
	private Long id;

	@Column(nullable = false) // Title must not be null
	private String title;

	@Column(nullable = false) // Description must not be null
	private String description;

	@Column(name = "created_date", nullable = false) // Timestamp of ticket creation
	private Date createdDate;

	@Column(nullable = false) // Ticket priority (HIGH, MEDIUM, LOW)
	private Priority priority;

	@Column(nullable = false) // Ticket status (OPEN, IN_PROGRESS, RESOLVED, etc.)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY, optional = false) // Many tickets belong to one customer
	@JoinColumn(name = "customer_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE) // If customer is deleted, their tickets are too
	@JsonIgnore // Prevent infinite recursion during serialization
	private User customer;

	@ManyToOne // Many tickets can be assigned to one agent
	@JoinColumn(name = "assigned_agent_id") // Nullable: can be unassigned
	private User assignedAgent;

	@ManyToOne // Many tickets can belong to one department
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;

	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL) // One ticket can have many comments
	private List<Comment> comments;

	// Converts Ticket entity to TicketDto for API responses
	public TicketDto getTicketDto() {
		TicketDto ticketDto = new TicketDto();
		ticketDto.setId(id);
		ticketDto.setTitle(title);
		ticketDto.setDescription(description);
		ticketDto.setCreatedDate(createdDate);
		ticketDto.setPriority(priority);
		ticketDto.setStatus(status);

		ticketDto.setCustomerId(customer.getId());
		ticketDto.setCustomerName(customer.getUsername());

		if (assignedAgent != null) {
			ticketDto.setAgentId(assignedAgent.getId());
			ticketDto.setAgentName(assignedAgent.getUsername());
		}

		ticketDto.setDepartmentId(department.getId());
		ticketDto.setDepartmentName(department.getName());

		// Convert Comment entities to CommentDto list
		if (comments != null) {
			ticketDto.setComments(comments.stream()
				.map(Comment::getCommentDto)
				.collect(Collectors.toList()));
		}

		return ticketDto;
	}
}
