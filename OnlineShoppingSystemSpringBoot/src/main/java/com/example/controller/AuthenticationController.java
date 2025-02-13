package com.example.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.AuthenticationService;
import com.example.authservice.JwtService;
import com.example.authservice.UserService;
import com.example.dto.LoginResponse;
import com.example.dto.LoginUserDto;
import com.example.dto.RegisterUserDto;
import com.example.model.User;
import com.example.model.UserInfo;

@RequestMapping("/auth")
@RestController
//@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {
	
	   @Autowired
		private UserService userService;
	   
    private final JwtService jwtService;
 
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
    	System.out.println("hi");
        User registeredUser = authenticationService.signup(registerUserDto);
        System.out.println("hi");
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        // Authenticate the user
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        // Convert the authenticated User to a UserDetails object
        UserDetails userDetails = new UserInfo(authenticatedUser);
        String email = userDetails.getUsername();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        System.out.println(roles);
        System.out.println(email);
        String role = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.startsWith("ROLE_") ? auth.substring(5) : auth) // Remove "ROLE_" prefix
                .findFirst()
                .orElse("UNKNOWN");
        System.out.println(role);
        Optional<User> user = userService.findbyemail(email);
        System.out.println(user);
        String username = user.get().getUsername();
        // Generate JWT token using UserDetails
        String jwtToken = jwtService.generateToken(userDetails);

        // Create the login response with the token and expiration time
        LoginResponse loginResponse = new LoginResponse()
        		.setUsername(username).setRole(role)	
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());

        // Return the response
        return ResponseEntity.ok(loginResponse);
    }
}
