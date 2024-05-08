package com.example.passwordmanager.Services;


import com.example.passwordmanager.Dto.LoginDto;
import com.example.passwordmanager.Dto.RegistrationDto;
import com.example.passwordmanager.Dto.TokenDto;
import com.example.passwordmanager.Entities.Role;
import com.example.passwordmanager.Entities.RoleNames;
import com.example.passwordmanager.Entities.User;
import com.example.passwordmanager.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public void registerUser(RegistrationDto registrationDto){
        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Role role = new Role();
        role.setName(registrationDto.getRole());
        user.setRole(role);
        userRepository.save(user);
    }

    public TokenDto login (LoginDto loginDto){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow();
        Role role = user.getRole();
        return TokenDto.builder().token(jwtUtil.generateToken((UserDetails) user,role)).build();
    }


}
