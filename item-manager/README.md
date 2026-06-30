# Item Manager

A Spring Boot application with OAuth2 authentication (Google & GitHub) and full CRUD operations.

## Features
-  OAuth2 Login with Google
-  OAuth2 Login with GitHub
-  Full CRUD Operations (Create, Read, Update, Delete)
-  PostgreSQL Database
-  User-specific data isolation

## Technologies
- Spring Boot 3.4.2
- Spring Security 6.4.2
- Spring Data JPA
- PostgreSQL
- OAuth2 Client

## Setup Instructions

### Prerequisites
- Java 21
- PostgreSQL
- Maven

### Configuration
1. Clone the repository
2. Copy `application.properties.template` to `application.properties`
3. Fill in your credentials:
    - PostgreSQL password
    - GitHub Client ID & Secret
    - Google Client ID & Secret
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
## API Endpoints
| Method | Endpoint |Description|
|--------|----------|-----------|
| POST | /api/items | Create a new item |
| GET | /api/items | Get all user's items |
| GET | /api/items/{id} | Get a specific item |
| PUT | /api/items{id} | Update an item |
|DELETE| /api/items{id}| Delete an item |

## Security
- All API endpoints require authentication
- Each user can only access their own items
- OAuth2 login with Google & GitHub
