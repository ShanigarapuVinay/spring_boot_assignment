package com.vinay.spring_boot_assignment.review_service.controller;

import com.vinay.spring_boot_assignment.review_service.model.Review;
import com.vinay.spring_boot_assignment.review_service.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private Review review1;
    private Review review2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        review1 = new Review(1L, "Great anime!", 5.0, LocalDateTime.now(), 1L);
        review2 = new Review(2L, "Awesome!", 4.5, LocalDateTime.now(), 1L);
    }


    @Test
    void GetAllReviews_ReturnsListOfReviews() {
        List<EntityModel<Review>> reviewList = Arrays.asList(
                EntityModel.of(review1),
                EntityModel.of(review2)
        );

        when(reviewService.findAll()).thenReturn(Arrays.asList(review1, review2));

        ResponseEntity<List<EntityModel<Review>>> result = reviewController.getAllReviews();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(reviewService, times(1)).findAll();
    }

    @Test
    void GetReviewById_ReturnsReview_WhenIdExists() {
        EntityModel<Review> reviewEntity = EntityModel.of(review1);
        when(reviewService.findById(1L)).thenReturn(Optional.of(review1));

        ResponseEntity<EntityModel<Review>> result = reviewController.getReviewById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(reviewService, times(1)).findById(1L);
    }

    @Test
    void GetReviewById_ReturnsNotFound_WhenIdDoesNotExist() {
        when(reviewService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<Review>> result = reviewController.getReviewById(1L);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(reviewService, times(1)).findById(1L);
    }

    @Test
    void GetReviewsByAnimeId_ReturnsListOfReviews_WhenAnimeIdExists() {
        List<EntityModel<Review>> reviews = Arrays.asList(
                EntityModel.of(review1),
                EntityModel.of(review2)
        );

        when(reviewService.findByAnimeId(1L)).thenReturn(Arrays.asList(review1, review2));

        ResponseEntity<List<EntityModel<Review>>> result = reviewController.getReviewsByAnimeId(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(reviewService, times(1)).findByAnimeId(1L);
    }

    @Test
    void CreateReview_ReturnsCreatedReview() {
        EntityModel<Review> reviewEntity = EntityModel.of(review1);
        when(reviewService.save(review1)).thenReturn(review1);

        ResponseEntity<EntityModel<Review>> result = reviewController.createReview(review1);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(reviewService, times(1)).save(review1);
    }

    @Test
    void UpdateReview_ReturnsUpdatedReview_WhenIdExists() {
        Review updatedReview = new Review(1L, "Updated content", 4.0, LocalDateTime.now(), 1L);
        EntityModel<Review> reviewEntity = EntityModel.of(updatedReview);
        when(reviewService.update(1L, updatedReview)).thenReturn(Optional.of(updatedReview));

        ResponseEntity<EntityModel<Review>> result = reviewController.updateReview(1L, updatedReview);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(reviewService, times(1)).update(1L, updatedReview);
    }

    @Test
    void UpdateReview_ReturnsNotFound_WhenIdDoesNotExist() {
        Review updatedReview = new Review(1L, "Updated content", 4.0, LocalDateTime.now(), 1L);
        when(reviewService.update(1L, updatedReview)).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<Review>> result = reviewController.updateReview(1L, updatedReview);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(reviewService, times(1)).update(1L, updatedReview);
    }

    @Test
    void DeleteReview_ReturnsSuccess_WhenIdExists() {
        when(reviewService.delete(1L)).thenReturn(true);

        ResponseEntity<String> result = reviewController.deleteReview(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Review deletion successful", result.getBody());
        verify(reviewService, times(1)).delete(1L);
    }

    @Test
    void DeleteReview_ReturnsNotFound_WhenIdDoesNotExist() {
        when(reviewService.delete(1L)).thenReturn(false);

        ResponseEntity<String> result = reviewController.deleteReview(1L);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Review deletion failed: Review not found", result.getBody());
        verify(reviewService, times(1)).delete(1L);
    }

    @Test
    void DeleteReviewsByAnimeId_ReturnsSuccess_WhenAnimeIdExists() {
        doNothing().when(reviewService).deleteByAnimeId(1L);

        ResponseEntity<String> result = reviewController.deleteReviewsByAnimeId(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("All reviews for the anime deleted successfully", result.getBody());
        verify(reviewService, times(1)).deleteByAnimeId(1L);
    }

    @Test
    void DeleteReviewsByAnimeId_ReturnsError_WhenExceptionOccurs() {
        doThrow(new RuntimeException("Database error")).when(reviewService).deleteByAnimeId(1L);

        ResponseEntity<String> result = reviewController.deleteReviewsByAnimeId(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Failed to delete reviews: Database error", result.getBody());

        verify(reviewService, times(1)).deleteByAnimeId(1L);
    }

}
