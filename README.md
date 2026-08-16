# Online Art Gallery System

## Overview
The Online Art Gallery System is a Spring Boot-based web application designed to facilitate the buying, selling, and showcasing of artwork. It provides a platform for artists to display their creations and for customers to browse, purchase, and review artwork.

## Features
- **User Management**: 
  - Role-based access control (Admin, Artist, Customer).
  - User registration, login, and profile management.
  - Two-factor authentication (2FA) support.
- **Artwork Management**:
  - Artists can upload, categorize, and manage their artwork.
  - Customers can browse, search, and filter artwork by category, price, and status.
  - Artwork ratings and reviews.
- **Shopping Cart**:
  - Add artwork to the cart and proceed to checkout.
  - Integration with payment gateways (e.g., Chapa).
- **Admin Panel**:
  - Manage users, roles, and artwork.
  - View and manage transactions and system statistics.
- **Notifications**:
  - Email notifications for account verification, password reset, and order updates.

## Technologies Used
- **Backend**: Java, Spring Boot, Hibernate, JPA
- **Frontend**: Thymeleaf (or integrate with Angular/React for a modern UI)
- **Database**: PostgreSQL
- **Security**: Spring Security, JWT, BCrypt Password Encoding
- **Build Tool**: Maven
- **Other Tools**: Lombok, Jackson, Hibernate Annotations

## Prerequisites
- Java 17 or higher
- Maven 3.8+
- PostgreSQL 14+
- IDE (e.g., IntelliJ IDEA)
- Git

## Installation and Setup
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/online-art-gallery.git
   cd online-art-gallery
   ```

2. **Configure the Database**:
    - Create a PostgreSQL database (e.g., `art_gallery`).
    - Update the database configuration in `src/main/resources/application.yml`:
      ```yaml
      spring:
        datasource:
          url: jdbc:postgresql://localhost:5432/art_gallery
          username: your_db_username
          password: your_db_password
      ```

3. **Build the Project**:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**:
    - Open your browser and navigate to `http://localhost:8088`.

## Seed Data
The application includes a data seeder (`DataSeederService`) to populate the database with sample users, roles, standards, and artwork. This is executed automatically on application startup.

## API contract

Frozen **v1.0** lives in [`docs/api-contract.md`](docs/api-contract.md). Vue and Spring both implement that document (`/api/v1` on port 8088, envelope `{ status, message, content }`, Chapa checkout).

- OpenAPI JSON: `http://localhost:8088/v3/api-docs`
- Swagger UI: `http://localhost:8088/swagger-ui.html`
- Frontend unwrap rules: `vue-oag-frontend/docs/api-contract.md`

Do not add endpoints without updating the contract. Legacy aliases are listed in that file under section H.

## Testing
- Unit and integration tests are written using JUnit and Mockito.
- To run tests:
  ```bash
  mvn test
  ```

## Deployment
1. **Docker**:
    - Build the Docker image:
      ```bash
      docker build -t online-art-gallery .
      ```
    - Run the container:
      ```bash
      docker run -p 8088:8088 online-art-gallery
      ```

2. **Cloud Deployment**:
    - Deploy to platforms like AWS, Azure, or Heroku.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch (`feature/your-feature-name`).
3. Commit your changes.
4. Push to your branch and create a pull request.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact
For any inquiries or support, please contact:
- **Email**: support@artgallery.com
- **Website**: [http://localhost:8088](http://localhost:8088)
```