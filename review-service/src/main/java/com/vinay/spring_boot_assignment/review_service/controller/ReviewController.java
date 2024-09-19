package com.vinay.spring_boot_assignment.review_service.controller;

import com.vinay.spring_boot_assignment.review_service.model.Review;
import com.vinay.spring_boot_assignment.review_service.service.ReviewService;
import com.vinay.spring_boot_assignment.review_service.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(Constants.REVIEW_BASE_URL)
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<EntityModel<Review>>> getAllReviews() {
        List<EntityModel<Review>> reviewList = reviewService.findAll().stream()
                .map(review -> EntityModel.of(review,
                        linkTo(methodOn(ReviewController.class).getReviewById(review.getId())).withSelfRel(),
                        linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("allReviews"),
                        linkTo(methodOn(ReviewController.class).getReviewsByAnimeId(review.getAnimeId())).withRel("reviewsByAnimeId")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping(Constants.REVIEW_ID)
    public ResponseEntity<EntityModel<Review>> getReviewById(@PathVariable Long id) {
        return reviewService.findById(id)
                .map(review -> EntityModel.of(review,
                        linkTo(methodOn(ReviewController.class).getReviewById(id)).withSelfRel(),
                        linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("allReviews"),
                        linkTo(methodOn(ReviewController.class).getReviewsByAnimeId(review.getAnimeId())).withRel("reviewsByAnimeId")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(Constants.ANIME_ANIME_ID)
    public ResponseEntity<List<EntityModel<Review>>> getReviewsByAnimeId(@PathVariable Long animeId) {
        List<EntityModel<Review>> reviews = reviewService.findByAnimeId(animeId).stream()
                .map(review -> EntityModel.of(review,
                        linkTo(methodOn(ReviewController.class).getReviewById(review.getId())).withSelfRel(),
                        linkTo(methodOn(ReviewController.class).getReviewsByAnimeId(animeId)).withRel("reviewsByAnimeId")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Review>> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.save(review);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EntityModel.of(savedReview,
                        linkTo(methodOn(ReviewController.class).getReviewById(savedReview.getId())).withSelfRel(),
                        linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("allReviews"),
                        linkTo(methodOn(ReviewController.class).getReviewsByAnimeId(savedReview.getAnimeId())).withRel("reviewsByAnimeId")));
    }

    @PutMapping(Constants.REVIEW_ID)
    public ResponseEntity<EntityModel<Review>> updateReview(@PathVariable Long id, @RequestBody Review reviewDetails) {
        return reviewService.update(id, reviewDetails)
                .map(updatedReview -> EntityModel.of(updatedReview,
                        linkTo(methodOn(ReviewController.class).getReviewById(updatedReview.getId())).withSelfRel(),
                        linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("allReviews"),
                        linkTo(methodOn(ReviewController.class).getReviewsByAnimeId(updatedReview.getAnimeId())).withRel("reviewsByAnimeId")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(Constants.REVIEW_ID)
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        boolean isDeleted = reviewService.delete(id);
        return isDeleted
                ? ResponseEntity.ok("Review deletion successful")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review deletion failed: Review not found");
    }

    @DeleteMapping(Constants.ANIME_ANIME_ID)
    public ResponseEntity<String> deleteReviewsByAnimeId(@PathVariable Long animeId) {
        try {
            reviewService.deleteByAnimeId(animeId);
            return ResponseEntity.ok("All reviews for the anime deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete reviews: " + e.getMessage());
        }
    }
}
