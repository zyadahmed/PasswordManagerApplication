package com.example.passwordmanager.Controller;

import com.example.passwordmanager.Dto.UserDto;
import com.example.passwordmanager.Entities.User;
import com.example.passwordmanager.Repositories.UserRepository;
import com.example.passwordmanager.Services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final ProfileService profile;


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable int id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && userDetails.getUsername().equals(user.get().getUsername())) {
            return ResponseEntity.ok(new UserDto(user.get()));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @PostMapping("/{id}")
    public ResponseEntity<UserDto> modifyUserData(@PathVariable int id, @RequestBody UserDto userDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        if (!userDetails.getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassCreationDate(userDto.getPassCreationDate());

        userRepository.save(user);

        return ResponseEntity.ok(new UserDto(user));
    }
    @PostMapping("/uploadImage")
    public ResponseEntity<String>uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        try
        {
            String imageData = profile.uploadUserImage(file);
            return ResponseEntity.ok(imageData);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }




}
