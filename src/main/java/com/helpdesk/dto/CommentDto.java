package com.helpdesk.dto;

import lombok.Data;
import java.util.Date;

/**
 * Data Transfer Object for transferring comment data between client and server.
 */
@Data  // Lombok annotation to generate boilerplate code (getters, setters, etc.)
public class CommentDto {

    // Unique identifier for the comment
    private Long id;

    // Content/body of the comment
    private String body;

    // Timestamp when the comment was created
    private Date createdAt;

    // ID of the user who posted the comment
    private Long userId;

    // Username or display name of the user who posted the comment
    private String postedBy;

    // ID of the ticket to which the comment belongs
    private Long ticketId;
}
