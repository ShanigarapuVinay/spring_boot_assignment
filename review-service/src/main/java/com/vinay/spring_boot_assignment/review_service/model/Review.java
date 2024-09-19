package com.vinay.spring_boot_assignment.review_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "anime_id")
    private Long animeId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
