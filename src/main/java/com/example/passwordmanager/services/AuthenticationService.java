package com.example.passwordmanager.services;


import com.example.passwordmanager.Conatiners.UnverifiedUserContainer;
import com.example.passwordmanager.dtos.*;
import com.example.passwordmanager.entities.RoleNames;
import com.example.passwordmanager.entities.User;
import com.example.passwordmanager.repositories.UserRepository;
import com.example.passwordmanager.exceptions.EmailSendingException;
import com.example.passwordmanager.exceptions.InvalidTokenException;
import com.example.passwordmanager.exceptions.UserNotFoundException;
import com.example.passwordmanager.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UnverifiedUserContainer unverifiedUserContainer;
    private final EmailSenderService mailSender;

    public ResponseEntity<String> registerUser(RegistrationDto registrationDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors occurred: ");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessage.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; ");
            }
            throw new ValidationException(errorMessage.toString());
        }

        User user = this.generateUser(registrationDto);
        UnverifiedUserToken userToken = new UnverifiedUserToken(Instant.now(), user);
        String key = UnverifiedUserContainer.generateUUID();

        Resource resource = new ClassPathResource("templates/VerifyMailTemplate.html");
        Path path = resource.getFile().toPath();
        String content = Files.readString(path);
        content = content.replace("{verification_link}", "http://localhost:8080/auth/verify/" + key);

        mailSender.sendEmailHtml(user.getEmail(), "Verify Mail", content);

        unverifiedUserContainer.addToken(key, userToken);

        return ResponseEntity.ok("Verify the email");
    }
    public ResponseEntity<String> verifyEmail(String token) throws Exception {
        UnverifiedUserToken unverifiedUserToken = (UnverifiedUserToken) unverifiedUserContainer.removeToken(token);

        if (unverifiedUserToken == null) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        Instant tokenCreationTime = unverifiedUserToken.getDate();
        Instant now = Instant.now();

        if (Duration.between(tokenCreationTime, now).toMinutes() > 15) {
            unverifiedUserContainer.removeToken(token);
            throw new InvalidTokenException("Token expired");
        }

        User user = unverifiedUserToken.getUser();
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new Exception("Failed to save user to database", e);
        }        unverifiedUserContainer.removeToken(token);

        return ResponseEntity.ok("Email verified successfully!");
    }
    public void forgotPassword(ForgetPasswordDto email) {
        Optional<User> optionalUser = userRepository.findByEmail(email.getEmail());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = optionalUser.get();

        String resetToken = UnverifiedUserContainer.generateUUID();
        UnverifiedUserToken userToken = new UnverifiedUserToken(Instant.now(), user);
        unverifiedUserContainer.addToken(resetToken, userToken);

        try {
            Resource resource = new ClassPathResource("templates/ForgetPasswordTemplate.html");
            Path path = resource.getFile().toPath();
            String content = Files.readString(path);
            content = content.replace("{reset_link}", "http://localhost:8080/auth/reset-password/" + resetToken);

            mailSender.sendEmailHtml(email.getEmail(), "Reset password", content);
        } catch (Exception e) {
            throw new EmailSendingException("Error sending reset password email", e);
        }
    }
    public void resetPassword(String token, String newPassword) {
        UnverifiedUserToken unverifiedUserToken = (UnverifiedUserToken) unverifiedUserContainer.removeToken(token);

        if (unverifiedUserToken == null) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        Instant tokenCreationTime = unverifiedUserToken.getDate();
        Instant now = Instant.now();

        if (Duration.between(tokenCreationTime, now).toMinutes() > 15) {
            throw new InvalidTokenException("Token expired");
        }

        User user = unverifiedUserToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    private void saveUser(User user) {
        userRepository.save(user);
    }

    public User generateUser(RegistrationDto registrationDto) {
        return User.builder()
                .name(registrationDto.getName())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .role(RoleNames.USER)
                .build();
    }



    public TokenDto login (LoginDto loginDto){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow();
        //Role role = user.getRole();
        return TokenDto.builder().token(jwtUtil.generateToken(user)).build();
    }


}
