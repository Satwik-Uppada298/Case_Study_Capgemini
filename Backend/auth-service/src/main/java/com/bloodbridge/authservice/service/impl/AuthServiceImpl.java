package com.bloodbridge.authservice.service.impl;

import com.bloodbridge.authservice.dto.JwtResponse;
import com.bloodbridge.authservice.dto.LoginRequest;
import com.bloodbridge.authservice.dto.MessageResponse;
import com.bloodbridge.authservice.dto.SignupRequest;
import com.bloodbridge.authservice.entity.User;
import com.bloodbridge.authservice.repository.UserRepository;
import com.bloodbridge.authservice.security.JwtUtils;
import com.bloodbridge.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
        
        return new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
    
    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new MessageResponse("Error: Username is already taken!");
        }
        
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }
        
        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        
        String strRole = signUpRequest.getRole();
        User.Role role;
        
        if (strRole == null) {
            role = User.Role.USER;
        } else {
            role = ("admin".equalsIgnoreCase(strRole)) ? User.Role.ADMIN : User.Role.USER;
        }
        
        user.setRole(role);
        userRepository.save(user);
        
        return new MessageResponse("User registered successfully!");
    }
}
