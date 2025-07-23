package com.helpdesk.enums;

/**
 * Enum representing the lifecycle status of a support ticket.
 * Helps track the current state of ticket resolution.
 */
public enum Status {
	PENDING,     // Ticket has been created but not yet addressed
	INPROGRESS,  // Ticket is actively being worked on
	RESOLVED,    // Issue has been addressed, waiting for confirmation or closure
	CLOSED       // Ticket is finalized and closed
}
