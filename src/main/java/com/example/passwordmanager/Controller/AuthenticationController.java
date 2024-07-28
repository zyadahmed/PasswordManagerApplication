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
    public ResponseEntity<String> register(@RequestBody @Valid RegistrationDto registrationDto, BindingResult bindingResult) throws Exception {
        return authService.registerUser(registrationDto, bindingResult);
    }
    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) throws Exception {
        return authService.verifyEmail(token);
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgetPasswordDto email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestParam String newPassword) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }



    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto){
            return ResponseEntity.ok(authService.login(loginDto));
    }
}
