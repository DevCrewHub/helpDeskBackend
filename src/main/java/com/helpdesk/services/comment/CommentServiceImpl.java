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

    @Override
    public CommentDto createComment(Long ticketId, String body) {
        User currentUser = jwtUtil.getLoggedInUser();
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        if (currentUser != null && optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();

            // Security Check: Only the ticket's customer or assigned agent can comment.
            boolean isCustomer = currentUser.getUserRole() == UserRole.CUSTOMER && ticket.getCustomer().getId().equals(currentUser.getId());
            boolean isAgent = currentUser.getUserRole() == UserRole.AGENT && ticket.getAssignedAgent() != null && ticket.getAssignedAgent().getId().equals(currentUser.getId());

            if (isCustomer || isAgent) {
                Comment comment = new Comment();
                comment.setBody(body);
                comment.setCreatedAt(new Date());
                comment.setTicket(ticket);
                comment.setUser(currentUser);

                return commentRepository.save(comment).getCommentDto();
            } else {
                // This error message is for development; in production, you might want a more generic "Access Denied".
                throw new RuntimeException("User is not authorized to comment on this ticket.");
            }
        }
        throw new RuntimeException("User or Ticket not found.");
    }

    @Override
    public List<CommentDto> getCommentsByTicketId(Long ticketId) {
        User currentUser = jwtUtil.getLoggedInUser();
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        if (currentUser != null && optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();

            // Security Check: Customer, assigned Agent, or an Admin can view comments.
            boolean isCustomer = currentUser.getUserRole() == UserRole.CUSTOMER && ticket.getCustomer().getId().equals(currentUser.getId());
            boolean isAgent = currentUser.getUserRole() == UserRole.AGENT && ticket.getAssignedAgent() != null && ticket.getAssignedAgent().getId().equals(currentUser.getId());
            boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;

            if (isCustomer || isAgent || isAdmin) {
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