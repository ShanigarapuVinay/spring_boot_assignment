package com.vinay.spring_boot_assignment.review_service.service;

import com.vinay.spring_boot_assignment.review_service.model.Review;
import com.vinay.spring_boot_assignment.review_service.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review1;
    private Review review2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        review1 = new Review(1L, "Great anime!", 5.0, LocalDateTime.now(), 1L);
        review2 = new Review(2L, "Awesome!", 4.5, LocalDateTime.now(), 1L);
    }

    @Test
    void FindAll_ReturnsListOfReviews() {
        List<Review> reviews = Arrays.asList(review1, review2);

        when(reviewRepository.findAll()).thenReturn(reviews);

        List<Review> result = reviewService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(review1.getContent(), result.get(0).getContent());
        assertEquals(review2.getContent(), result.get(1).getContent());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void FindById_ReturnsReview_WhenIdExists() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review1));

        Optional<Review> result = reviewService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(review1.getContent(), result.get().getContent());
        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    void FindById_ReturnsEmpty_WhenIdDoesNotExist() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Review> result = reviewService.findById(1L);

        assertFalse(result.isPresent());
        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    void FindByAnimeId_ReturnsListOfReviews_WhenAnimeIdExists() {
        List<Review> reviews = Arrays.asList(review1, review2);

        when(reviewRepository.findByAnimeId(1L)).thenReturn(reviews);

        List<Review> result = reviewService.findByAnimeId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(review1.getContent(), result.get(0).getContent());
        verify(reviewRepository, times(1)).findByAnimeId(1L);
    }

    @Test
    void Save_ReturnsSavedReview() {
        when(reviewRepository.save(review1)).thenReturn(review1);

        Review result = reviewService.save(review1);

        assertNotNull(result);
        assertEquals("Great anime!", result.getContent());
        verify(reviewRepository, times(1)).save(review1);
    }

    @Test
    void Update_ReturnsUpdatedReview_WhenIdExists() {
        Review newReview = new Review(1L, "Updated content", 4.0, LocalDateTime.now(), 1L);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review1));
        when(reviewRepository.save(review1)).thenReturn(review1);

        Optional<Review> result = reviewService.update(1L, newReview);

        assertTrue(result.isPresent());
        assertEquals("Updated content", result.get().getContent());
        assertEquals(4.0, result.get().getRating());
        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(review1);
    }

    @Test
    void Update_ReturnsEmpty_WhenIdDoesNotExist() {
        Review newReview = new Review(1L, "Updated content", 4.0, LocalDateTime.now(), 1L);
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Review> result = reviewService.update(1L, newReview);

        assertFalse(result.isPresent());
        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void Delete_ReturnsTrue_WhenIdExists() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review1));

        boolean result = reviewService.delete(1L);

        assertTrue(result);
        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).delete(review1);
    }

    @Test
    void Delete_ReturnsFalse_WhenIdDoesNotExist() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = reviewService.delete(1L);

        assertFalse(result);
        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, never()).delete(any(Review.class));
    }

    @Test
    void DeleteByAnimeId_DeletesReviews_WhenAnimeIdExists() {
        reviewService.deleteByAnimeId(1L);

        verify(reviewRepository, times(1)).deleteByAnimeId(1L);
    }
}
