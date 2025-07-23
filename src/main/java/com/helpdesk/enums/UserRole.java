package com.helpdesk.enums;

/**
 * Enum representing roles that a user can have in the HelpDesk system.
 * Used for access control and permission management.
 */
public enum UserRole {
	ADMIN,    // Has full access to manage users, tickets, and system settings
	CUSTOMER, // Can create tickets and view their own ticket history
	AGENT     // Assigned to resolve tickets and respond to customer issues
}
