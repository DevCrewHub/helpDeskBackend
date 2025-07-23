package com.helpdesk.repositories;

import com.helpdesk.entities.Comment;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Retrieves all comments associated with a specific ticket by ticket ID
    List<Comment> findAllByTicketId(Long ticketId);

    // Retrieves all comments made by a specific user
    List<Comment> findByUser(User user);

    // Retrieves all comments associated with a specific ticket entity
    List<Comment> findByTicket(Ticket ticket);
}
