package com.vinay.spring_boot_assignment.review_service.service;

import com.vinay.spring_boot_assignment.review_service.model.Review;
import com.vinay.spring_boot_assignment.review_service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;

    @Override
    public List<Review> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Review> findByAnimeId(Long animeId) {
        return repository.findByAnimeId(animeId);
    }

    @Override
    public Review save(Review review) {
        return repository.save(review);
    }

    @Override
    public Optional<Review> update(Long id, Review newReview) {
        return repository.findById(id)
                .map(review -> {
                    review.setContent(newReview.getContent());
                    review.setRating(newReview.getRating());
                    review.setAnimeId(newReview.getAnimeId());
                    return repository.save(review);
                });
    }

    @Override
    public boolean delete(Long id) {
        return repository.findById(id)
                .map(review -> {
                    repository.delete(review);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    @Override
    public void deleteByAnimeId(Long animeId) {
        repository.deleteByAnimeId(animeId);
    }
}
