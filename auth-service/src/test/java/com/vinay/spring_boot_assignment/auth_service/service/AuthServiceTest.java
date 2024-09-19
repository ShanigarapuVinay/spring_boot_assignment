package com.vinay.spring_boot_assignment.auth_service.service;

import com.vinay.spring_boot_assignment.auth_service.model.UserCredentials;
import com.vinay.spring_boot_assignment.auth_service.repository.UserCredentialsRepository;
import com.vinay.spring_boot_assignment.auth_service.util.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserCredentialsRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private UserCredentials user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserCredentials();
        user.setName("testUser");
        user.setPassword("password123");
    }

    @Test
    void SaveUser_ReturnsSuccessMessage() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(repository.save(any(UserCredentials.class))).thenReturn(user);

        String result = authService.saveUser(user);

        assertEquals("User added to the System", result);
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(repository, times(1)).save(any(UserCredentials.class));
    }

    @Test
    void GenerateToken_ReturnsToken() {
        String username = "testUser";
        String expectedToken = "jwt-token";

        when(jwtService.generateToken(username)).thenReturn(expectedToken);

        String result = authService.generateToken(username);

        assertEquals(expectedToken, result);
        verify(jwtService, times(1)).generateToken(username);
    }

    @Test
    void ValidateToken_DoesNotThrowException() {
        String token = "validToken";

        doNothing().when(jwtService).validateToken(token);

        assertDoesNotThrow(() -> authService.validateToken(token));

        verify(jwtService, times(1)).validateToken(token);
    }

    @Test
    void ValidateToken_InvalidToken_ThrowsException() {
        String token = "invalidToken";

        doThrow(new RuntimeException("Invalid token")).when(jwtService).validateToken(token);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.validateToken(token));

        assertEquals("Invalid token", exception.getMessage());
        verify(jwtService, times(1)).validateToken(token);
    }
}
