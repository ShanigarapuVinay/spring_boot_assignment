package com.vinay.spring_boot_assignment.anime_service.service;

import com.vinay.spring_boot_assignment.anime_service.dto.ReviewDTO;
import com.vinay.spring_boot_assignment.anime_service.model.Anime;

import java.util.List;
import java.util.Optional;

public interface AnimeService {
    List<Anime> findAll();
    Optional<Anime> findById(Long id);
    Anime save(Anime anime);
    Optional<Anime> update(Long id, Anime animeDetails);
    boolean delete(Long id);
    List<ReviewDTO> getReviewsForAnime(Long animeId);
    ReviewDTO createReviewForAnime(Long animeId, ReviewDTO reviewDTO);
    ReviewDTO updateReviewForAnime(Long animeId, Long reviewId, ReviewDTO reviewDTO);
    void deleteReviewForAnime(Long animeId, Long reviewId);
}
