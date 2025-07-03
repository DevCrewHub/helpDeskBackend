# HelpDeskPro
HelpDesk Pro is a web-based application designed to facilitate efficient
communication between customers and support agents. Customers can submit support
tickets for issues or inquiries, and agents can view, manage, and respond to these tickets.
This project will expose students to crucial concepts like distinct user roles, state
management (ticket statuses), collaborative features (comments), and efficient data
handling in a business context.

## Features
- User registration (Customer)
- User login with JWT authentication
- Role-based access control (Admin, Customer, Agent)
- Secure password storage (BCrypt)
- RESTful API endpoints
- Department management by admin
- Agent signup with department selection by name
- Ticket creation by customer (pending status)
- Admin can view all tickets, view ticket details, assign tickets to agents, and search/filter tickets by title, priority, or status
- Agent can view tickets assigned to them and assign to themselves
- Agent can update ticket status
- Customer can close the ticket by changing it's status to closed

## Technologies Used
- Java 17+
- Spring Boot
- Spring Security (JWT)
- Hibernate/JPA
- Lombok
- MySQL

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven

### Setup
1. **Clone the repository:**
   ```sh
   git clone https://github.com/DevCrewHub/helpDeskBackend
   cd HelpDeskPro
   ```
2. **Configure the database:**
   The application uses MySQL as its database. Configure the following properties in `src/main/resources/application.properties` for your DB settings (MySQL):
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/helpdesk
    spring.datasource.username=root
    spring.datasource.password=your_password
    ```
3. **Build and run the application:**
   ```sh
   ./mvnw spring-boot:run
   # or on Windows
   mvnw.cmd spring-boot:run
   ```
4. **The app will start on** `http://localhost:8082` **(or your configured port).**

### API Endpoints

#### Auth APIs
- `POST /api/auth/signup` — Register a new user
   
   **Request Body Example:**
  ```json
  {
    "userName": "cust1",
    "email": "cust1@example.com",
    "password": "password1234",
    "fullName": "Customer One",
    "phoneNumber": "+1234567890"
  }
  ```
- `POST /api/auth/login` — Authenticate and receive a JWT token

   **Request Body Example:**
  ```json
  {
    "userName": "cust1",
    "password": "password1234"
  }
  ```

#### Admin APIs

- `POST /api/admin/agent` — Register a new agent

  **Request Body Example:**
  ```json
  {
    "userName": "agent1",
    "email": "agent1@example.com",
    "password": "password123",
    "fullName": "Agent One",
    "phoneNumber": "+1234567890",
    "departmentName": "Finance"
  }
  ```
- `GET /api/admin/customers` — Get all customers
- `GET /api/admin/agents` — Get all agents

##### Department Admin APIs
- `GET /api/admin/department` — Get all departments
- `GET /api/admin/department/{id}` — Get department by ID
- `POST /api/admin/department` — Create a new department
- `PUT /api/admin/department/{id}` — Update a department
- `DELETE /api/admin/department/{id}` — Delete a department

##### Ticket Control APIs (Admin)
- `GET /api/admin/tickets` — Get all tickets
- `GET /api/admin/ticket/{id}` — Get ticket details by ticket ID
- `PUT /api/admin/tickets/{ticketId}/assign?agentId={agentId}` — Assign a ticket to an agent
- `GET /api/admin/tickets/search/{title}` — Search tickets by title
- `GET /api/admin/tickets/priority/{priority}` — Filter tickets by priority (`LOW`, `MEDIUM`, `HIGH`)
- `GET /api/admin/tickets/status/{status}` — Filter tickets by status (`PENDING`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`)

#### Customer APIs
- `POST /api/customer/ticket` — Create a new ticket

  **Request Body Example:**
  ```json
  {
    "title": "Cannot access email",
    "description": "I am unable to access my company email account.",
    "priority": "HIGH",
    "departmentName": "Technical"
  }
  ```

- `GET /api/customer/tickets` — Get all tickets created by the logged-in customer
- `GET /api/customer/ticket/{id}` — Get a specific ticket by its ID (only if created by the logged-in customer)
- `GET /api/customer/tickets/search/{title}` — Search tickets by title (created by the logged-in customer)
- `PUT /api/customer/ticket/{id}/status` — Change the status of a ticket to CLOSED (only if created by the logged-in customer)

  *Note: Only status change to CLOSED is allowed. Once closed, the ticket cannot be updated further.*

- `DELETE /api/customer/ticket/{id}` — Delete a ticket by its ID (only if created by the logged-in customer)

#### Agent APIs

- `GET /api/agent/tickets` — Get all tickets assigned to the logged-in agent
- `GET /api/agent/ticket/{id}` — Get details of a specific ticket assigned to the agent
- `GET /api/agent/tickets/search/{title}` — Search assigned tickets by title
- `GET /api/agent/tickets/priority/{priority}` — Filter assigned tickets by priority (`LOW`, `MEDIUM`, `HIGH`)
- `GET /api/agent/tickets/status/{status}` — Filter assigned tickets by status (`PENDING`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`)
- `GET /api/agent/tickets/department/{name}` — Filter assigned tickets by department name (partial or full match)
- `PUT /api/agent/tickets/{ticketId}/priority?priority={priority}` — Update the priority of an assigned ticket
- `PUT /api/agent/tickets/{ticketId}/status?status={status}` — Update the status of an assigned ticket(inprogress to resolved)

## Project Structure
```
HelpDeskPro/
├── src/main/java/com/helpdesk/
│   ├── config/           # Security and JWT config
│   ├── controller/       # REST controllers
│   ├── dto/              # Data transfer objects
│   ├── entities/         # JPA entities
│   ├── enums/            # Enum types (UserRole, Status, Priority)
│   ├── repositories/     # Spring Data JPA repositories
│   ├── services/         # Service layer
│   └── utils/            # Utility classes (JWT)
├── src/main/resources/
│   └── application.properties
└── ...
```

## Contribution Guidelines
1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request