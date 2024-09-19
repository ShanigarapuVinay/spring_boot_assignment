package com.vinay.spring_boot_assignment.anime_service.controller;

import com.vinay.spring_boot_assignment.anime_service.dto.ReviewDTO;
import com.vinay.spring_boot_assignment.anime_service.model.Anime;
import com.vinay.spring_boot_assignment.anime_service.service.AnimeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@ExtendWith(MockitoExtension.class)
public class AnimeControllerTest {

    @Mock
    private AnimeService animeService;

    @InjectMocks
    private AnimeController animeController;

    @Test
    void GetAllAnime_ReturnsListOfAnime_WhenAnimeExist() {
        Anime anime1 = new Anime(1L, "Naruto", "Some description", "Action", LocalDate.now(), 500);
        Anime anime2 = new Anime(2L, "One Piece", "Some description", "Adventure", LocalDate.now(), 500);
        List<Anime> animeList = Arrays.asList(anime1, anime2);

        when(animeService.findAll()).thenReturn(animeList);

        ResponseEntity<List<EntityModel<Anime>>> response = animeController.getAllAnime();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(animeService, times(1)).findAll();
    }

    @Test
    void GetAnimeById_ReturnsAnime_WhenIdIsValid() {
        Anime anime = new Anime(1L, "Naruto", "Some description", "Action", LocalDate.now(), 500);
        when(animeService.findById(1L)).thenReturn(Optional.of(anime));

        ResponseEntity<EntityModel<Anime>> response = animeController.getAnimeById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(anime.getId(), response.getBody().getContent().getId());
        verify(animeService, times(1)).findById(1L);
    }

    @Test
    void GetAnimeById_ReturnsNotFound_WhenIdIsInvalid() {
        when(animeService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<Anime>> response = animeController.getAnimeById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(animeService, times(1)).findById(1L);
    }

    @Test
    void CreateAnime_CreatesAnime_WhenDataIsValid() {
        Anime anime = new Anime(1L, "Naruto", "Some description", "Action", LocalDate.now(), 500);
        when(animeService.save(anime)).thenReturn(anime);

        ResponseEntity<EntityModel<Anime>> response = animeController.createAnime(anime);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(anime.getId(), response.getBody().getContent().getId());
        verify(animeService, times(1)).save(anime);
    }

    @Test
    void UpdateAnime_UpdatesAnime_WhenIdIsValid() {
        Anime anime = new Anime(1L, "Naruto", "Some description", "Action", LocalDate.now(), 500);
        when(animeService.update(1L, anime)).thenReturn(Optional.of(anime));

        ResponseEntity<EntityModel<Anime>> response = animeController.updateAnime(1L, anime);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(animeService, times(1)).update(1L, anime);
    }

    @Test
    void UpdateAnime_ReturnsNotFound_WhenIdIsInvalid() {
        Anime anime = new Anime(1L, "Naruto", "Some description", "Action", LocalDate.now(), 500);
        when(animeService.update(1L, anime)).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<Anime>> response = animeController.updateAnime(1L, anime);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(animeService, times(1)).update(1L, anime);
    }

    @Test
    void DeleteAnime_DeletesAnime_WhenIdIsValid() {
        when(animeService.delete(1L)).thenReturn(true);

        ResponseEntity<String> response = animeController.deleteAnime(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(animeService, times(1)).delete(1L);
    }

    @Test
    void DeleteAnime_ReturnsNotFound_WhenIdIsInvalid() {
        when(animeService.delete(1L)).thenReturn(false);

        ResponseEntity<String> response = animeController.deleteAnime(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(animeService, times(1)).delete(1L);
    }

    @Test
    void GetReviewsForAnime_ReturnsListOfEntityModelReviews_WhenAnimeExists() {
        ReviewDTO review1 = new ReviewDTO();
        review1.setId(1L);
        review1.setContent("Great anime!");
        review1.setRating(5.0);
        review1.setAnimeId(1L);

        ReviewDTO review2 = new ReviewDTO();
        review2.setId(2L);
        review2.setContent("Awesome!");
        review2.setRating(5.0);
        review2.setAnimeId(1L);

        List<ReviewDTO> reviews = Arrays.asList(review1, review2);

        when(animeService.getReviewsForAnime(1L)).thenReturn(reviews);

        ResponseEntity<List<EntityModel<ReviewDTO>>> response = animeController.getReviewsForAnime(1L);

        assertNotNull(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(2, response.getBody().size());

        EntityModel<ReviewDTO> entityModel1 = response.getBody().get(0);
        assertEquals(review1.getId(), entityModel1.getContent().getId());
        assertEquals(review1.getContent(), entityModel1.getContent().getContent());
        assertEquals(review1.getRating(), entityModel1.getContent().getRating());

        EntityModel<ReviewDTO> entityModel2 = response.getBody().get(1);
        assertEquals(review2.getId(), entityModel2.getContent().getId());
        assertEquals(review2.getContent(), entityModel2.getContent().getContent());
        assertEquals(review2.getRating(), entityModel2.getContent().getRating());

        verify(animeService, times(1)).getReviewsForAnime(1L);
    }


    @Test
    void CreateReviewForAnime_CreatesReview_WhenDataIsValid() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setContent("Great anime!");
        reviewDTO.setRating(5.0);
        reviewDTO.setAnimeId(1L);

        when(animeService.createReviewForAnime(1L, reviewDTO)).thenReturn(reviewDTO);

        ResponseEntity<EntityModel<ReviewDTO>> response = animeController.createReviewForAnime(1L, reviewDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(animeService, times(1)).createReviewForAnime(1L, reviewDTO);
    }

    @Test
    void UpdateReviewForAnime_UpdatesReview_WhenReviewAndAnimeExist() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setContent("Updated review");
        reviewDTO.setRating(4.0);
        reviewDTO.setAnimeId(1L);

        when(animeService.updateReviewForAnime(1L, 1L, reviewDTO)).thenReturn(reviewDTO);

        ResponseEntity<EntityModel<ReviewDTO>> response = animeController.updateReviewForAnime(1L, 1L, reviewDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(animeService, times(1)).updateReviewForAnime(1L, 1L, reviewDTO);
    }

    @Test
    void DeleteReviewForAnime_DeletesReview_WhenReviewExists() {
        doNothing().when(animeService).deleteReviewForAnime(1L, 1L);

        ResponseEntity<String> response = animeController.deleteReviewForAnime(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(animeService, times(1)).deleteReviewForAnime(1L, 1L);
    }

    @Test
    void DeleteReviewForAnime_ReturnsNotFound_WhenReviewNotFound() {
        doThrow(new RuntimeException("Review not found")).when(animeService).deleteReviewForAnime(1L, 1L);

        ResponseEntity<String> response = animeController.deleteReviewForAnime(1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Review not found"));
        verify(animeService, times(1)).deleteReviewForAnime(1L, 1L);
    }
}
