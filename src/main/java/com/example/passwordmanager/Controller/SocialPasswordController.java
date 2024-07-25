package com.example.passwordmanager.Controller;

import com.example.passwordmanager.Dto.SocialPasswordDTO;
import com.example.passwordmanager.Entities.Password;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
            return ResponseEntity.ok("Social password saved successfully: " );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving social password");
        }
    }
    @GetMapping("/getAllSocial")
    public ResponseEntity<List<Password>> getAllUserSocialPasswords(Authentication authentication){
        String username = authentication.getName();
        Optional<User> user = passwordService.getAllUserPassword(username);
        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Social> socials = user.get().getPasswordList()
                .stream().filter(password -> password instanceof  Social)
                .map(password -> (Social)password)
                .toList();
        return ResponseEntity.ok(user.get().getPasswordList());

    }
    @GetMapping("/{id}")
    public ResponseEntity<SocialPasswordDTO> getSocialPasswordById(@PathVariable int id , Authentication authentication){
        String username = authentication.getName();
        Optional<Social> socialOptional = passwordService.findSocialPasswordById(id);
        if (socialOptional.isEmpty()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Social social = socialOptional.get();
        if (!Objects.equals(social.getUser().getEmail(), username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        SocialPasswordDTO socialPasswordDTO = new SocialPasswordDTO();
        socialPasswordDTO.setName(social.getName());
        socialPasswordDTO.setDescription(social.getDescription());
        socialPasswordDTO.setPassword(social.getPassword());
        socialPasswordDTO.setVerticationCode(social.getVerticationCode());

        return ResponseEntity.ok(socialPasswordDTO);


    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSocialPasswordById(@PathVariable int id, Authentication authentication) {
        String username = authentication.getName();
        Optional<Social> socialOptional = passwordService.findSocialPasswordById(id);
        if (socialOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Social social = socialOptional.get();
        if (!Objects.equals(social.getUser().getEmail(), username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        passwordService.deleteSocialPasswordById(id);
        return ResponseEntity.ok("Password deleted successfully");
    }










}
