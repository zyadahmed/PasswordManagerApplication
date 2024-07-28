package com.example.passwordmanager.controller;

import com.example.passwordmanager.Conatiners.UnverifiedUserContainer;
import com.example.passwordmanager.dtos.*;
import com.example.passwordmanager.repositories.UserRepository;
import com.example.passwordmanager.services.AuthenticationService;
import com.example.passwordmanager.services.EmailSenderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
