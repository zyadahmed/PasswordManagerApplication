# Password Manager API

## Overview

The Password Manager API is a RESTful service for managing user authentication and password storage. It provides CRUD operations for passwords, user login, registration, and features for password recovery and verification using SMTP mail. The API ensures security by storing credentials and encryption keys using Doppler and an in-memory map for temporary email verification.

## Features

- User Registration
- User Login
- Create, Read , Delete  operations for passwords
- Password Recovery (Forgot Password)
- Password Verification
- SMTP mail integration for password recovery and verification
- Secure credential storage using Doppler
- Encryption key management

## Technologies Used

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- SMTP Mail (JavaMail)
- Doppler (for managing secrets and credentials)
- PostgreSQL (Render)

## Getting Started


### Setup Instructions

1. **Clone the repository:**

    ```bash
    git clone https://github.com/your-username/password-manager-api.git
    cd password-manager-api
    ```

2. **Configure Doppler:**

    Install and configure Doppler CLI as per the [Doppler documentation](https://docs.doppler.com/docs/install-cli). Ensure your project is set up in Doppler with the required secrets such as database credentials, SMTP credentials, and encryption keys.

3. **Set environment variables:**

    Create a `.env` file in the project root and add the following environment variables:

    ```plaintext
    DOPPLER_TOKEN=your_doppler_token
    ```

4. **Update application properties:**

    Ensure your `application.properties` or `application.yml` is set up to use Doppler for secrets. Example:

    ```properties
    spring.datasource.url=${DOPPLER_DATABASE_URL}
    spring.datasource.username=${DOPPLER_DATABASE_USERNAME}
    spring.datasource.password=${DOPPLER_DATABASE_PASSWORD}
    spring.mail.host=${DOPPLER_SMTP_HOST}
    spring.mail.port=${DOPPLER_SMTP_PORT}
    spring.mail.username=${DOPPLER_SMTP_USERNAME}
    spring.mail.password=${DOPPLER_SMTP_PASSWORD}
    ```

5. **Build and run the application:**

    ```bash
    mvn clean install
    doppler run -- ./mvnw spring-boot:run
    ```

## API Endpoints

### Authentication

- **Register:** `POST /auth/register`  
  Request Body: `RegistrationDto`
  - Registers a new user and sends a verification email.
  
- **Verify Email:** `GET /auth/verify/{token}`  
  Path Variable: `token`
  - Verifies the user's email using the provided token.
  
- **Login:** `POST /auth/login`  
  Request Body: `LoginDto`
  - Authenticates a user and returns a JWT token.
  
- **Forgot Password:** `POST /auth/forgot-password`  
  Request Body: `ForgetPasswordDto`
  - Sends a password reset email.
  
- **Reset Password:** `POST /auth/reset-password/{token}`  
  Path Variable: `token`
  Request Param: `newPassword`
  - Resets the user's password using the provided token and new password.
  ### Password Management

- **Create Social Password:** `POST /user/socialPasswords/add`  
  Request Body: `SocialPasswordDTO`
  - Creates a new social password for the authenticated user.
  
- **Get All Social Passwords:** `GET /user/socialPasswords/getAllSocial`  
  - Retrieves all social passwords for the authenticated user.
  
- **Get Social Password by ID:** `GET /user/socialPasswords/{id}`  
  Path Variable: `id`
  - Retrieves a specific social password by ID for the authenticated user.
  
- **Delete Social Password by ID:** `DELETE /user/socialPasswords/{id}`  
  Path Variable: `id`
  - Deletes a specific social password by ID for the authenticated user.

### User Management

- **Get User by ID:** `GET /user/{id}`  
  Path Variable: `id`
  - Retrieves the user information by ID for the authenticated user.
  
- **Modify User Data:** `POST /user/{id}`  
  Path Variable: `id`
  Request Body: `UserDto`
  - Modifies the user data for the authenticated user.



