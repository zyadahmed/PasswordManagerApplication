package com.example.passwordmanager.services;

import com.example.passwordmanager.entities.ImageData;
import com.example.passwordmanager.entities.User;
import com.example.passwordmanager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final StorageService storageService;
    private final UserRepository userRepository;

    public String uploadUserImage(MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        String uploadResponse = storageService.uploadImage(file);
        if(uploadResponse!=null){
            user.builder()
                    .imageData(ImageData.builder()
                            .name(file.getOriginalFilename())
                            .type(file.getContentType())
                            .imageData(file.getBytes())
                            .build())
                    .build();
            userRepository.save(user);
        }
        return uploadResponse;
    }

}
