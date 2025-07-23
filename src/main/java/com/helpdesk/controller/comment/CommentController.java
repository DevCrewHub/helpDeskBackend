package com.helpdesk.controller.comment;

import com.helpdesk.dto.CommentDto;
import com.helpdesk.services.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller that handles HTTP requests related to comments on tickets.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments") // Base path for comment-related APIs
public class CommentController {

    private final CommentService commentService; // Service for comment operations

    /**
     * Endpoint to create a new comment on a ticket.
     * 
     * @param commentDto the comment data including ticket ID and body
     * @return ResponseEntity with created comment or error message
     */
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentDto commentDto) {
        try {
            // Attempt to create a comment using ticket ID and comment body
            CommentDto createdComment = commentService.createComment(commentDto.getTicketId(), commentDto.getBody());

            // If comment creation fails, return bad request
            if (createdComment == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create comment.");
            }

            // Return created comment with HTTP 201 status
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);

        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Endpoint to retrieve all comments for a given ticket ID.
     * 
     * @param ticketId the ID of the ticket
     * @return ResponseEntity with list of comments or error message
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<?> getCommentsByTicket(@PathVariable Long ticketId) {
        try {
            // Retrieve comments for the given ticket ID
            List<CommentDto> comments = commentService.getCommentsByTicketId(ticketId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            // Return error response if retrieval fails
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
