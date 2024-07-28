package com.example.passwordmanager.services;

import com.example.passwordmanager.entities.ImageData;
import com.example.passwordmanager.repositories.StorageRepository;
import com.example.passwordmanager.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;

    public String uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = storageRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if(imageData != null){
            return "file uploaded Successfully : " + file.getOriginalFilename();
        }
        return null;
    }
}
