package com.example.passwordmanager.Controller;

import com.example.passwordmanager.Dto.UserDto;
import com.example.passwordmanager.Entities.User;
import com.example.passwordmanager.Repositories.UserRepository;
import com.example.passwordmanager.Services.ProfileService;
import com.example.passwordmanager.Services.UserService;
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
    private final UserService userService;

    private final ProfileService profile;


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable int id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDto userDto = userService.getUserById(id, userDetails);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserDto> modifyUserData(@PathVariable int id, @RequestBody UserDto userDto, Authentication authentication) throws Exception {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDto updatedUserDto = userService.modifyUserData(id, userDto, userDetails);
        return ResponseEntity.ok(updatedUserDto);
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
