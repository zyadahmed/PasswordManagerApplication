package com.example.passwordmanager.Controller;

import com.example.passwordmanager.Dto.LoginDto;
import com.example.passwordmanager.Dto.RegistrationDto;
import com.example.passwordmanager.Dto.TokenDto;
import com.example.passwordmanager.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    final private AuthenticationService authService;
    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody RegistrationDto registrationDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            // Construct error message
            StringBuilder errorMessage = new StringBuilder("Validation errors occurred: ");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessage.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        try {
            authService.registerUser(registrationDto);
            return ResponseEntity.ok("Registration successful");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed.");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto){
            return ResponseEntity.ok(authService.login(loginDto));
    }


}
