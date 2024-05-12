package com.example.passwordmanager.Services;


import com.example.passwordmanager.Dto.LoginDto;
import com.example.passwordmanager.Dto.RegistrationDto;
import com.example.passwordmanager.Dto.TokenDto;
import com.example.passwordmanager.Entities.RoleNames;
import com.example.passwordmanager.Entities.User;
import com.example.passwordmanager.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        User user = User.builder()
                .name(registrationDto.getName())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .role(RoleNames.valueOf(registrationDto.getRole())).build();
        userRepository.save(user);
    }
    /*
    public ResponseEntity<String> registerUser(RegistrationDto registrationDto) {
        // Validate registrationDto fields here if needed
        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        RoleNames roleName = RoleNames.valueOf(String.valueOf(registrationDto.getRole()));
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }

        user.setRole(role);

        userRepository.save(user);
        return null;
    }
     */

    public TokenDto login (LoginDto loginDto){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow();
        //Role role = user.getRole();
        return TokenDto.builder().token(jwtUtil.generateToken(user)).build();
    }


}
