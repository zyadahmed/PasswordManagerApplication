package com.example.passwordmanager.controller;

import com.example.passwordmanager.dtos.SocialPasswordDTO;
import com.example.passwordmanager.entities.Social;
import com.example.passwordmanager.repositories.UserRepository;
import com.example.passwordmanager.services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/socialPasswords")
public class SocialPasswordController {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/add")
    public ResponseEntity<Social> createSocialPassword(@RequestBody SocialPasswordDTO socialPasswordDTO) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        Social social = passwordService.saveSocialPassword(socialPasswordDTO, currentUser.getUsername());
        return ResponseEntity.ok(social);
    }

    @GetMapping("/getAllSocial")
    public ResponseEntity<List<SocialPasswordDTO>> getAllUserSocialPasswords(Authentication authentication) {
        String username = authentication.getName();
        List<SocialPasswordDTO> socialPasswordDTOs = passwordService.getAllUserSocialPasswords(username);
        return ResponseEntity.ok(socialPasswordDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocialPasswordDTO> getSocialPasswordById(@PathVariable int id, Authentication authentication) {
        String username = authentication.getName();
        SocialPasswordDTO socialPasswordDTO = passwordService.getSocialPasswordById(id, username);
        return ResponseEntity.ok(socialPasswordDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSocialPasswordById(@PathVariable int id, Authentication authentication) {
        String username = authentication.getName();
        passwordService.deleteSocialPasswordById(id, username);
        return ResponseEntity.ok("Password deleted successfully");
    }










}
