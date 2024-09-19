package com.vinay.spring_boot_assignment.auth_service.service;

import com.vinay.spring_boot_assignment.auth_service.model.UserCredentials;
import com.vinay.spring_boot_assignment.auth_service.repository.UserCredentialsRepository;
import com.vinay.spring_boot_assignment.auth_service.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialsRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String saveUser(UserCredentials credentials){
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        repository.save(credentials);
        return "User added to the System";
    }

    public String generateToken(String username){
        return jwtService.generateToken(username);
    }

    public void validateToken(String token){
        jwtService.validateToken(token);
    }
}
