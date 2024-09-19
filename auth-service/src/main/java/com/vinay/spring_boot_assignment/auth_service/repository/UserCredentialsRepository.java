package com.vinay.spring_boot_assignment.auth_service.repository;

import com.vinay.spring_boot_assignment.auth_service.model.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    Optional<UserCredentials> findByName(String name);
}
