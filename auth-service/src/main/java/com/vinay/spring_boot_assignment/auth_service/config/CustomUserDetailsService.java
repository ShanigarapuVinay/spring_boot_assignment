package com.vinay.spring_boot_assignment.auth_service.config;

import com.vinay.spring_boot_assignment.auth_service.model.UserCredentials;
import com.vinay.spring_boot_assignment.auth_service.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialsRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredentials> credentials = repository.findByName(username);
        return credentials.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));
    }
}
