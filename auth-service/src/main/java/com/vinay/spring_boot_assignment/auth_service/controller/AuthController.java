package com.vinay.spring_boot_assignment.auth_service.controller;

import com.vinay.spring_boot_assignment.auth_service.dto.AuthRequest;
import com.vinay.spring_boot_assignment.auth_service.exception.InvalidCredentialsException;
import com.vinay.spring_boot_assignment.auth_service.model.UserCredentials;
import com.vinay.spring_boot_assignment.auth_service.service.AuthService;
import com.vinay.spring_boot_assignment.auth_service.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.AUTH_BASE_URL)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    @PostMapping(Constants.REGISTER)
    public String addNewUser(@RequestBody UserCredentials user){
        return authService.saveUser(user);
    }

    @PostMapping(Constants.TOKEN)
    public String getToken(@RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if(authenticate.isAuthenticated()) {
            return authService.generateToken(authRequest.getUsername());
        }else {
            throw new InvalidCredentialsException("Invalid Access! User Doesn't exist or credentials are incorrect.");
        }
    }

    @GetMapping(Constants.VALIDATE)
    public String validateToken(@RequestParam("token") String token){
        authService.validateToken(token);
        return "Token is Valid";
    }
}
