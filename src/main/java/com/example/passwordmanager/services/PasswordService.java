package com.example.passwordmanager.services;

import com.example.passwordmanager.dtos.SocialPasswordDTO;
import com.example.passwordmanager.entities.Social;
import com.example.passwordmanager.entities.User;
import com.example.passwordmanager.repositories.PasswordRepository;
import com.example.passwordmanager.repositories.UserRepository;
import com.example.passwordmanager.exceptions.DatabaseSaveException;
import com.example.passwordmanager.exceptions.UnauthorizedAccessException;
import com.example.passwordmanager.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PasswordService {
    private PasswordRepository passwordRepository;
    private UserRepository userRepository;
    private EncryptionService encryptionService;



    @Transactional
    public Social saveSocialPassword(SocialPasswordDTO socialPasswordDTO, String username) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Optional<User> userOpt = userRepository.findByEmail(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        Social social = new Social();
        social.setName(socialPasswordDTO.getName());
        social.setDescription(socialPasswordDTO.getDescription());
        social.setPassword(encryptionService.encrypt(socialPasswordDTO.getPassword()));
        social.setVerticationCode(socialPasswordDTO.getVerticationCode());

        social.setUser(user);
        user.addPassword(social);

        userRepository.save(user);

        return social;
    }

        @Transactional
        public List<SocialPasswordDTO> getAllUserSocialPasswords(String username) {
            Optional<User> optionalUser = userRepository.findUserWithPasswords(username);
            if (optionalUser.isEmpty()) {
                throw new UserNotFoundException("User not found");
            }
            User user = optionalUser.get();
            return user.getPasswordList().stream()
                    .filter(password -> password instanceof Social)
                    .map(password -> {
                        Social social = (Social) password;
                        decryptPassword(social);
                        return new SocialPasswordDTO(social);
                    })
                    .collect(Collectors.toList());
        }

        private void decryptPassword(Social social) {
            try {
                social.setPassword(encryptionService.decrypt(social.getPassword()));
            } catch (Exception e) {
                throw new DatabaseSaveException("Failed to decrypt password", e);
            }
        }

        public SocialPasswordDTO getSocialPasswordById(int id, String username) {
            Optional<Social> socialOptional = passwordRepository.findById(id).map(Social.class::cast);
            if (socialOptional.isEmpty()) {
                throw new UserNotFoundException("Password not found");
            }
            Social social = socialOptional.get();
            if (!social.getUser().getEmail().equals(username)) {
                throw new UnauthorizedAccessException("You are not authorized to access this password");
            }
            decryptPassword(social);
            return new SocialPasswordDTO(social);
        }

        public void deleteSocialPasswordById(int id, String username) {
            Optional<Social> socialOptional = passwordRepository.findById(id).map(Social.class::cast);
            if (socialOptional.isEmpty()) {
                throw new UserNotFoundException("Password not found");
            }
            Social social = socialOptional.get();
            if (!social.getUser().getEmail().equals(username)) {
                throw new UnauthorizedAccessException("You are not authorized to delete this password");
            }
            passwordRepository.deleteById(id);
        }
}






