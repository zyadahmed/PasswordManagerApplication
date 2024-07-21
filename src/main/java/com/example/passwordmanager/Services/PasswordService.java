package com.example.passwordmanager.Services;

import com.example.passwordmanager.Dto.SocialPasswordDTO;
import com.example.passwordmanager.Entities.Password;
import com.example.passwordmanager.Entities.Social;
import com.example.passwordmanager.Entities.User;
import com.example.passwordmanager.Repositories.PasswordRepository;
import com.example.passwordmanager.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PasswordService {
    @Autowired
    private PasswordRepository passwordRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Social saveSocialPassword(SocialPasswordDTO socialPasswordDTO, String username) {
        Optional<User> userOpt = userRepository.findByEmail(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        Social social = new Social();
        social.setName(socialPasswordDTO.getName());
        social.setDescription(socialPasswordDTO.getDescription());
        social.setPassword(socialPasswordDTO.getPassword());
        social.setVerticationCode(socialPasswordDTO.getVerticationCode());
        social.setUser(user);

        return passwordRepository.save(social);
    }
    public Optional<Social> findSocialPasswordById(int id) {
        return passwordRepository.findById(id).map(Social.class::cast);
    }
    public void deleteSocialPasswordById(int id) {
        passwordRepository.deleteById(id);
    }
}
