package com.example.passwordmanager.Controller;

import com.example.passwordmanager.Conatiners.UnverifiedUserContainer;
import com.example.passwordmanager.Dto.LoginDto;
import com.example.passwordmanager.Dto.RegistrationDto;
import com.example.passwordmanager.Dto.TokenDto;
import com.example.passwordmanager.Dto.UnverifiedUserToken;
import com.example.passwordmanager.Entities.User;
import com.example.passwordmanager.Repositories.UserRepository;
import com.example.passwordmanager.Services.AuthenticationService;
import com.example.passwordmanager.Services.EmailSenderService;
import com.example.passwordmanager.Services.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

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
            // Construct error message
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
            mailSender.sendEmail(user.getEmail(),"Vertifcation Email","http://localhost:8080/auth/verify/"+key);
            unverifiedUserContainer.addUnvertifedUser(key,userToken);

            return ResponseEntity.ok("verify the email");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed.");
        }
    }
    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) {
        UnverifiedUserToken unverifiedUserToken = unverifiedUserContainer.removeUnvertifiedUser(token);

        if (unverifiedUserToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        Instant tokenCreationTime = unverifiedUserToken.getDate();
        Instant now = Instant.now();

        if (Duration.between(tokenCreationTime, now).toMinutes() > 15) {
            unverifiedUserContainer.removeUnvertifiedUser(token);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired");
        }

        User user = unverifiedUserToken.getUser();
        userRepository.save(user);
        unverifiedUserContainer.removeUnvertifiedUser(token);

        return ResponseEntity.ok("Email verified successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto){
            return ResponseEntity.ok(authService.login(loginDto));
    }
}
