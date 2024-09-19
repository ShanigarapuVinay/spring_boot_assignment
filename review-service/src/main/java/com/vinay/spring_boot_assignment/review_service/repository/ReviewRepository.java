package com.vinay.spring_boot_assignment.review_service.repository;

import com.vinay.spring_boot_assignment.review_service.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAnimeId(Long animeId);
    void deleteByAnimeId(Long animeId);
}
