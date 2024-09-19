package com.vinay.spring_boot_assignment.auth_service.controller;

import com.vinay.spring_boot_assignment.auth_service.dto.AuthRequest;
import com.vinay.spring_boot_assignment.auth_service.exception.InvalidCredentialsException;
import com.vinay.spring_boot_assignment.auth_service.model.UserCredentials;
import com.vinay.spring_boot_assignment.auth_service.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void AddNewUser_ReturnsSuccessMessage() {
        UserCredentials user = new UserCredentials();
        user.setName("testUser");
        user.setPassword("password123");

        when(authService.saveUser(any(UserCredentials.class))).thenReturn("User added to the System");

        String response = authController.addNewUser(user);

        assertEquals("User added to the System", response);
        verify(authService, times(1)).saveUser(any(UserCredentials.class));
    }

    @Test
    void GetToken_ReturnsToken() {
        AuthRequest authRequest = new AuthRequest("testUser", "password123");
        String token = "jwt-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authService.generateToken(anyString())).thenReturn(token);

        String response = authController.getToken(authRequest);

        assertEquals(token, response);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authService, times(1)).generateToken(anyString());
    }

    @Test
    void GetToken_InvalidCredentials_ThrowsInvalidCredentialsException() {
        AuthRequest authRequest = new AuthRequest("invalidUser", "wrongPassword");

        Authentication failedAuthentication = mock(Authentication.class);
        when(failedAuthentication.isAuthenticated()).thenReturn(false);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(failedAuthentication);

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            authController.getToken(authRequest);
        });

        assertEquals("Invalid Access! User Doesn't exist or credentials are incorrect.", exception.getMessage());

        verify(authService, never()).generateToken(anyString());
    }

    @Test
    void ValidateToken_ReturnsValidMessage() {
        String token = "validToken";

        doNothing().when(authService).validateToken(token);

        String response = authController.validateToken(token);

        assertEquals("Token is Valid", response);
        verify(authService, times(1)).validateToken(token);
    }
}
