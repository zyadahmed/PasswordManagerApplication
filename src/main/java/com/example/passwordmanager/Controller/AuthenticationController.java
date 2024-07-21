package com.example.passwordmanager.Controller;

import com.example.passwordmanager.Conatiners.UnverifiedUserContainer;
import com.example.passwordmanager.Dto.*;
import com.example.passwordmanager.Entities.User;
import com.example.passwordmanager.Repositories.UserRepository;
import com.example.passwordmanager.Services.AuthenticationService;
import com.example.passwordmanager.Services.EmailSenderService;
import com.example.passwordmanager.Services.JwtService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    final private AuthenticationService authService;
    private final UnverifiedUserContainer unverifiedUserContainer;
    private final EmailSenderService mailSender;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody @Valid RegistrationDto registrationDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors occurred: ");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessage.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        try {
            User user = authService.genrateUser(registrationDto);
            UnverifiedUserToken userToken = new UnverifiedUserToken(Instant.now(),user);
            String key = UnverifiedUserContainer.generateUUID();


            Resource resource = new ClassPathResource("templates/VerifyMailTemplate.html");
            Path path = resource.getFile().toPath();
            String content = Files.readString(path);
            content = content.replace("{verification_link}", "http://localhost:8080/auth/verify/"+key);

            mailSender.sendEmailHtml(user.getEmail(), "Verify Mail",content);

            unverifiedUserContainer.addToken(key, userToken);

            return ResponseEntity.ok("verify the email");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed.");
        }
    }
    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) {
        UnverifiedUserToken unverifiedUserToken = (UnverifiedUserToken) unverifiedUserContainer.removeToken(token);

        if (unverifiedUserToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        Instant tokenCreationTime = unverifiedUserToken.getDate();
        Instant now = Instant.now();

        if (Duration.between(tokenCreationTime, now).toMinutes() > 15) {
            unverifiedUserContainer.removeToken(token);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired");
        }

        User user = unverifiedUserToken.getUser();
        userRepository.save(user);
        unverifiedUserContainer.removeToken(token);

        return ResponseEntity.ok("Email verified successfully!");
    }


    // reset password

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgetPasswordDto email) throws IOException, MessagingException {
        Optional<User> optionalUser = userRepository.findByEmail(email.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();
        System.out.println(user);

        String resetToken = UnverifiedUserContainer.generateUUID();
        UnverifiedUserToken userToken = new UnverifiedUserToken(Instant.now(), user); // Assuming UnverifiedUserToken constructor takes Instant and User
        unverifiedUserContainer.addToken(resetToken, userToken);

        Resource resource = new ClassPathResource("templates/ForgetPasswordTemplate.html");
        Path path = resource.getFile().toPath();
        String content = Files.readString(path);
        content = content.replace("{reset_link}", "http://localhost:8080/auth/reset-password/" + resetToken);

        mailSender.sendEmailHtml(email.getEmail(), "Reset password",content);

        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestParam String newPassword) {
        UnverifiedUserToken unverifiedUserToken = (UnverifiedUserToken) unverifiedUserContainer.removeToken(token);

        if (unverifiedUserToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        Instant tokenCreationTime = unverifiedUserToken.getDate();
        Instant now = Instant.now();

        if (Duration.between(tokenCreationTime, now).toMinutes() > 15) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired");
        }

        User user = unverifiedUserToken.getUser();
        userRepository.updatePassword(user, newPassword);

        return ResponseEntity.ok("Password reset successfully");
    }



    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto){
            return ResponseEntity.ok(authService.login(loginDto));
    }
}
