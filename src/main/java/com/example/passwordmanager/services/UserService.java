package com.example.passwordmanager.services;

import com.example.passwordmanager.dtos.UserDto;
import com.example.passwordmanager.entities.User;
import com.example.passwordmanager.repositories.UserRepository;
import com.example.passwordmanager.exceptions.UnauthorizedAccessException;
import com.example.passwordmanager.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;

    public UserDto getUserById(int id, UserDetails userDetails) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && userDetails.getUsername().equals(user.get().getUsername())) {
            return new UserDto(user.get());
        } else if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + id);
        } else {
            throw new UnauthorizedAccessException("You are not authorized to access this user's data");
        }
    }

    public UserDto modifyUserData(int id, UserDto userDto, UserDetails userDetails) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        User user = optionalUser.get();
        if (!userDetails.getUsername().equals(user.getUsername())) {
            throw new UnauthorizedAccessException("You are not authorized to modify this user's data");
        }

        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassCreationDate(userDto.getPassCreationDate());

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new Exception("Failed to save user to database");
        }

        return new UserDto(user);
    }
}
