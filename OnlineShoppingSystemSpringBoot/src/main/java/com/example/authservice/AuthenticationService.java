package com.example.authservice;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.LoginUserDto;
import com.example.dto.RegisterUserDto;
import com.example.model.Address;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        // Ensure all required fields from the DTO are not null
        if (input.getUsername() == null || input.getEmail() == null || input.getPassword() == null) {
            throw new IllegalArgumentException("Required fields are missing");
        }

        User user = new User()
                .setUserName(input.getUsername())   // Corrected getter method
                .setEmail(input.getEmail())
                .setRole(Role.USER)  // Assuming default role
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setPhoneNumber(input.getPhoneNumber());
                Address address = new Address();
                address.setStreet(input.getStreet());
                address.setCity(input.getCity());
                address.setState(input.getState());
                address.setCountry(input.getCountry());
                address.setZipCode(input.getZipCode());
                
                // Add the address to the user
                user.getAddresses().add(address);
              

        return userRepository.save(user);
    }



    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
