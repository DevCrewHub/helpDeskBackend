package com.helpdesk.entities;

import com.helpdesk.dto.CommentDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity // Marks this class as a JPA entity
@Data   // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class Comment {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID values
    private Long id;

    @Lob // Allows storage of large text content
    private String body;

    // Timestamp of when the comment was created
    private Date createdAt;

    // Many comments can belong to one ticket
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false) // Foreign key to ticket table
    @OnDelete(action = OnDeleteAction.CASCADE) // Deletes comments when associated ticket is deleted
    private Ticket ticket;

    // Many comments can be made by one user
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to user table
    @OnDelete(action = OnDeleteAction.CASCADE) // Deletes comments when associated user is deleted
    private User user;

    // Converts Comment entity to CommentDto
    public CommentDto getCommentDto() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(id);
        commentDto.setBody(body);
        commentDto.setCreatedAt(createdAt);
        commentDto.setTicketId(ticket.getId());
        commentDto.setUserId(user.getId());
        commentDto.setPostedBy(user.getFullName()); // Sets the name of the user who posted the comment
        return commentDto;
    }
}
