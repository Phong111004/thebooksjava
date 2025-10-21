# Migration Plan: Node.js to Java Spring Boot with UDP Sockets

## Overview
Migrate the entire Node.js Express backend to Java Spring Boot, ensuring all functions and data integrity are preserved. Incorporate UDP sockets for real-time broadcasting (e.g., book updates to clients).

## Steps
- [x] Set up Spring Boot project structure (pom.xml, application.properties)
- [x] Create JPA entities (User, Book, Category, ReadingHistory)
- [x] Create repositories for data access
- [x] Create services for business logic
- [x] Create REST controllers for all endpoints (books, categories, users)
- [x] Implement authentication with Spring Security and BCrypt
- [x] Configure CORS and static file serving
- [x] Implement UDP broadcasting service for real-time updates
- [ ] Test DB connection and all endpoints
- [ ] Verify data integrity and function preservation
- [ ] Update build scripts (remove Node.js, add Java build)

## Testing Checklist
- DB connection: Ensure SQL Server connection works.
- Endpoints: All GET/POST/PUT/DELETE for books, categories, users, history.
- Auth: Login/register with password hashing.
- UDP: Broadcasting works (e.g., notify clients on book add/update).
- Static files: Images, CSS, JS served correctly.
- CORS: Frontend can access APIs.

## Notes
- DB schema unchanged; data preserved.
- Use UDP for broadcasting to optimize for speed (e.g., DatagramSocket in Java).
- Clients may need to listen for UDP packets or integrate with WebSockets if needed.
