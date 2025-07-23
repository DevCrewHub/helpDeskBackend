package com.helpdesk.services.comment;

import com.helpdesk.dto.CommentDto;
import java.util.List;

public interface CommentService {

    /**
     * Creates a new comment for the given ticket.
     *
     * @param ticketId the ID of the ticket to which the comment will be added
     * @param body     the content of the comment
     * @return the created CommentDto
     */
    CommentDto createComment(Long ticketId, String body);

    /**
     * Retrieves all comments associated with a specific ticket.
     *
     * @param ticketId the ID of the ticket whose comments are to be fetched
     * @return a list of CommentDto objects
     */
    List<CommentDto> getCommentsByTicketId(Long ticketId);

}
