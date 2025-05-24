package com.bloodbridge.authservice.service;

import com.bloodbridge.authservice.dto.JwtResponse;
import com.bloodbridge.authservice.dto.LoginRequest;
import com.bloodbridge.authservice.dto.MessageResponse;
import com.bloodbridge.authservice.dto.SignupRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    MessageResponse registerUser(SignupRequest signUpRequest);
}
