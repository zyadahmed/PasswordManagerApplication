package com.example.passwordmanager.Controller;

import com.example.passwordmanager.Dto.SocialPasswordDTO;
import com.example.passwordmanager.Entities.Social;
import com.example.passwordmanager.Entities.User;
import com.example.passwordmanager.Repositories.UserRepository;
import com.example.passwordmanager.Services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/socialPasswords")
public class SocialPasswordController {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<String> createSocialPassword(@RequestBody SocialPasswordDTO socialPasswordDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String username = currentUser.getUsername();

        try {
            Social savedSocial = passwordService.saveSocialPassword(socialPasswordDTO, username);
            return ResponseEntity.ok("Social password saved successfully: " + savedSocial.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving social password");
        }
    }
}
