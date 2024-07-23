package com.example.passwordmanager.Services;

import com.example.passwordmanager.Dto.SocialPasswordDTO;
import com.example.passwordmanager.Entities.Social;
import com.example.passwordmanager.Entities.User;
import com.example.passwordmanager.Repositories.PasswordRepository;
import com.example.passwordmanager.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PasswordService {
    @Autowired
    private PasswordRepository passwordRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
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

        user.addPassword(social);

        userRepository.save(user);

        return passwordRepository.save(social);
    }


    @Transactional
    public Optional<User> getAllUserPassword(String username) {
        Optional<User> optionalUser = userRepository.findUserWithPasswords(username);

        optionalUser.ifPresent(user ->
                user.getPasswordList().forEach(password -> {
                    if (password instanceof Social) {
                        Social social = (Social) password;
                        try {
                            // Decrypt the password
                            social.setPassword(encryptionService.decrypt(social.getPassword()));
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to decrypt password", e);
                        }
                    }
                })
        );

        return optionalUser;
    }




    public Optional<Social> findSocialPasswordById(int id) {
        return passwordRepository.findById(id).map(Social.class::cast);
    }
    public void deleteSocialPasswordById(int id) {
        passwordRepository.deleteById(id);
    }
}
