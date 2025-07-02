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
- Admin can view and assign pending tickets to agents
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

#### Department Admin APIs
- `GET /api/admin/department` — Get all departments
- `GET /api/admin/department/{id}` — Get department by ID
- `POST /api/admin/department` — Create a new department
- `PUT /api/admin/department/{id}` — Update a department
- `DELETE /api/admin/department/{id}` — Delete a department

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