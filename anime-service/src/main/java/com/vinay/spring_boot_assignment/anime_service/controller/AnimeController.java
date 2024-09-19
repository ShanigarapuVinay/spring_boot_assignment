package com.vinay.spring_boot_assignment.anime_service.controller;

import com.vinay.spring_boot_assignment.anime_service.dto.ReviewDTO;
import com.vinay.spring_boot_assignment.anime_service.model.Anime;
import com.vinay.spring_boot_assignment.anime_service.service.AnimeService;
import com.vinay.spring_boot_assignment.anime_service.util.Constants;
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
@RequiredArgsConstructor
@RequestMapping(Constants.ANIME_BASE_URL)
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<EntityModel<Anime>>> getAllAnime() {
        List<EntityModel<Anime>> animeList = animeService.findAll().stream()
                .map(anime -> EntityModel.of(anime,
                        linkTo(methodOn(AnimeController.class).getAnimeById(anime.getId())).withSelfRel(),
                        linkTo(methodOn(AnimeController.class).getReviewsForAnime(anime.getId())).withRel("reviews"),
                        linkTo(methodOn(AnimeController.class).getAllAnime()).withRel("allAnime")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(animeList);
    }


    @GetMapping(Constants.ANIME_ID)
    public ResponseEntity<EntityModel<Anime>> getAnimeById(@PathVariable Long id) {
        return animeService.findById(id)
                .map(anime -> EntityModel.of(anime,
                        linkTo(methodOn(AnimeController.class).getAnimeById(id)).withSelfRel(),
                        linkTo(methodOn(AnimeController.class).getReviewsForAnime(id)).withRel("reviews"),
                        linkTo(methodOn(AnimeController.class).getAllAnime()).withRel("allAnime")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<Anime>> createAnime(@RequestBody Anime anime) {
        Anime createdAnime = animeService.save(anime);

        EntityModel<Anime> animeModel = EntityModel.of(createdAnime,
                linkTo(methodOn(AnimeController.class).getAnimeById(createdAnime.getId())).withSelfRel(),
                linkTo(methodOn(AnimeController.class).getAllAnime()).withRel("allAnime"));

        return ResponseEntity.status(HttpStatus.CREATED).body(animeModel);
    }

    @PutMapping(Constants.ANIME_ID)
    public ResponseEntity<EntityModel<Anime>> updateAnime(@PathVariable Long id, @RequestBody Anime animeDetails) {
        return animeService.update(id, animeDetails)
                .map(updatedAnime -> EntityModel.of(updatedAnime,
                        linkTo(methodOn(AnimeController.class).getAnimeById(id)).withSelfRel(),
                        linkTo(methodOn(AnimeController.class).getAllAnime()).withRel("allAnime")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(Constants.ANIME_ID)
    public ResponseEntity<String> deleteAnime(@PathVariable Long id) {
        boolean isDeleted = animeService.delete(id);
        if (isDeleted)
            return ResponseEntity.ok("Anime and its reviews deleted successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete anime. Anime with id " + id + " not found.");
    }

    @GetMapping(Constants.ANIME_ID_REVIEWS)
    public ResponseEntity<List<EntityModel<ReviewDTO>>> getReviewsForAnime(@PathVariable Long animeId) {
        List<EntityModel<ReviewDTO>> reviews = animeService.getReviewsForAnime(animeId).stream()
                .map(review -> EntityModel.of(review,
                        linkTo(methodOn(AnimeController.class).getReviewsForAnime(animeId)).withSelfRel(),
                        linkTo(methodOn(AnimeController.class).getAnimeById(animeId)).withRel("anime")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviews);
    }

    @PostMapping(Constants.ANIME_ID_REVIEWS)
    public ResponseEntity<EntityModel<ReviewDTO>> createReviewForAnime(@PathVariable Long animeId, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = animeService.createReviewForAnime(animeId, reviewDTO);

        EntityModel<ReviewDTO> reviewModel = EntityModel.of(createdReview,
                linkTo(methodOn(AnimeController.class).getReviewsForAnime(animeId)).withRel("reviews"),
                linkTo(methodOn(AnimeController.class).getAnimeById(animeId)).withRel("anime"));

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewModel);
    }

    @PutMapping(Constants.ANIME_ID_REVIEWS_REVIEW_ID)
    public ResponseEntity<EntityModel<ReviewDTO>> updateReviewForAnime(@PathVariable Long animeId, @PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = animeService.updateReviewForAnime(animeId, reviewId, reviewDTO);

        EntityModel<ReviewDTO> reviewModel = EntityModel.of(updatedReview,
                linkTo(methodOn(AnimeController.class).getReviewsForAnime(animeId)).withRel("reviews"),
                linkTo(methodOn(AnimeController.class).getAnimeById(animeId)).withRel("anime"));

        return ResponseEntity.ok(reviewModel);
    }

    @DeleteMapping(Constants.ANIME_ID_REVIEWS_REVIEW_ID)
    public ResponseEntity<String> deleteReviewForAnime(@PathVariable Long animeId, @PathVariable Long reviewId) {
        try {
            animeService.deleteReviewForAnime(animeId, reviewId);
            return ResponseEntity.ok("Review deleted successfully for anime_id : " + animeId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
