package com.helpdesk.services.comment;

import com.helpdesk.dto.CommentDto;
import com.helpdesk.entities.Comment;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.enums.UserRole;
import com.helpdesk.repositories.CommentRepository;
import com.helpdesk.repositories.TicketRepository;
import com.helpdesk.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final TicketRepository ticketRepository;
    private final JwtUtil jwtUtil;
    private final CommentRepository commentRepository;

    /**
     * Creates a comment on a ticket.
     * Only the ticket's customer or the assigned agent is authorized to comment.
     *
     * @param ticketId the ID of the ticket to comment on
     * @param body     the content of the comment
     * @return the created CommentDto
     */
    @Override
    public CommentDto createComment(Long ticketId, String body) {
        // Get the currently logged-in user
        User currentUser = jwtUtil.getLoggedInUser();
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        // Proceed only if user and ticket exist
        if (currentUser != null && optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();

            // Check if current user is the customer of the ticket
            boolean isCustomer = currentUser.getUserRole() == UserRole.CUSTOMER &&
                    ticket.getCustomer().getId().equals(currentUser.getId());

            // Check if current user is the assigned agent of the ticket
            boolean isAssignedAgent = currentUser.getUserRole() == UserRole.AGENT &&
                    ticket.getAssignedAgent() != null &&
                    ticket.getAssignedAgent().getId().equals(currentUser.getId());

            // Allow comment creation only if user is customer or assigned agent
            if (isCustomer || isAssignedAgent) {
                Comment comment = new Comment();
                comment.setBody(body);
                comment.setCreatedAt(new Date());
                comment.setTicket(ticket);
                comment.setUser(currentUser);

                // Save the comment and return its DTO
                return commentRepository.save(comment).getCommentDto();
            } else {
                throw new RuntimeException("User is not authorized to comment on this ticket.");
            }
        }

        throw new RuntimeException("User or Ticket not found.");
    }

    /**
     * Retrieves all comments for a given ticket.
     * Accessible by customer, assigned agent, other agents, and admin.
     *
     * @param ticketId the ID of the ticket
     * @return a list of CommentDto
     */
    @Override
    public List<CommentDto> getCommentsByTicketId(Long ticketId) {
        // Get the currently logged-in user
        User currentUser = jwtUtil.getLoggedInUser();
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        // Proceed only if user and ticket exist
        if (currentUser != null && optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();

            // Check if user is customer and owns the ticket
            boolean isCustomer = currentUser.getUserRole() == UserRole.CUSTOMER &&
                    ticket.getCustomer().getId().equals(currentUser.getId());

            // Check if user is the assigned agent
            boolean isAssignedAgent = currentUser.getUserRole() == UserRole.AGENT &&
                    ticket.getAssignedAgent() != null &&
                    ticket.getAssignedAgent().getId().equals(currentUser.getId());

            // Check if user is any agent (not necessarily assigned)
            boolean isAgent = currentUser.getUserRole() == UserRole.AGENT;

            // Check if user is admin
            boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;

            // Allow viewing if user is customer, assigned agent, any agent, or admin
            if (isCustomer || isAssignedAgent || isAgent || isAdmin) {
                return commentRepository.findAllByTicketId(ticketId).stream()
                        .map(Comment::getCommentDto)
                        .collect(Collectors.toList());
            } else {
                throw new RuntimeException("User is not authorized to view comments on this ticket.");
            }
        }

        throw new RuntimeException("User or Ticket not found.");
    }
}
