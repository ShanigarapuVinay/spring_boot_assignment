package com.vinay.spring_boot_assignment.anime_service.service;

import com.vinay.spring_boot_assignment.anime_service.dto.ReviewDTO;
import com.vinay.spring_boot_assignment.anime_service.model.Anime;
import com.vinay.spring_boot_assignment.anime_service.repository.AnimeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimeServiceTest {

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AnimeServiceImpl animeService;

    @Test
    void Find_AllAnime_ReturnsListOfAnime() {
        List<Anime> animeList = Arrays.asList(new Anime(), new Anime());
        when(animeRepository.findAll()).thenReturn(animeList);

        List<Anime> result = animeService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(animeRepository, times(1)).findAll();
    }

    @Test
    void Find_AnimeById_ReturnsAnime_WhenIdIsValid() {
        Anime anime = new Anime(1L, "Naruto", "Shonen", "Action", null, 220);
        when(animeRepository.findById(1L)).thenReturn(Optional.of(anime));

        Optional<Anime> result = animeService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Naruto", result.get().getTitle());
        verify(animeRepository, times(1)).findById(1L);
    }

    @Test
    void Find_AnimeById_ReturnsEmpty_WhenIdIsInvalid() {
        when(animeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Anime> result = animeService.findById(99L);

        assertFalse(result.isPresent());
        verify(animeRepository, times(1)).findById(99L);
    }

    @Test
    void Save_Anime_ReturnsSavedAnime() {
        Anime anime = new Anime(null, "Naruto", "Shonen", "Action", null, 220);
        when(animeRepository.save(anime)).thenReturn(anime);

        Anime result = animeService.save(anime);

        assertNotNull(result);
        assertEquals("Naruto", result.getTitle());
        verify(animeRepository, times(1)).save(anime);
    }

    @Test
    void Update_AnimeById_UpdatesAnime_WhenIdIsValid() {
        Anime existingAnime = new Anime(1L, "Naruto", "Shonen", "Action", null, 220);
        Anime updatedAnime = new Anime(null, "Naruto Shippuden", "Shonen", "Action", null, 500);
        when(animeRepository.findById(1L)).thenReturn(Optional.of(existingAnime));
        when(animeRepository.save(any(Anime.class))).thenReturn(updatedAnime);

        Optional<Anime> result = animeService.update(1L, updatedAnime);

        assertTrue(result.isPresent());
        assertEquals("Naruto Shippuden", result.get().getTitle());
        verify(animeRepository, times(1)).findById(1L);
        verify(animeRepository, times(1)).save(any(Anime.class));
    }

    @Test
    void Update_AnimeById_ReturnsEmpty_WhenIdIsInvalid() {
        when(animeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Anime> result = animeService.update(99L, new Anime());

        assertFalse(result.isPresent());
        verify(animeRepository, times(1)).findById(99L);
        verify(animeRepository, never()).save(any());
    }

    @Test
    void Delete_AnimeById_DeletesAnime_WhenIdIsValid() {
        Anime anime = new Anime(1L, "Naruto", "Shonen", "Action", null, 220);
        when(animeRepository.findById(1L)).thenReturn(Optional.of(anime));

        boolean result = animeService.delete(1L);

        assertTrue(result);
        verify(animeRepository, times(1)).findById(1L);
        verify(animeRepository, times(1)).delete(anime);
        verify(restTemplate, times(1)).delete(anyString());
    }

    @Test
    void Delete_AnimeById_ReturnsFalse_WhenIdIsInvalid() {
        when(animeRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = animeService.delete(99L);

        assertFalse(result);
        verify(animeRepository, times(1)).findById(99L);
        verify(animeRepository, never()).delete(any());
        verify(restTemplate, never()).delete(anyString());
    }

    @Test
    void GetReviews_ForAnime_ReturnsListOfReviews_WhenAnimeExists() {
        Anime anime = new Anime(1L, "Naruto", "Shonen", "Action", null, 220);
        when(animeRepository.findById(1L)).thenReturn(Optional.of(anime));
        List<ReviewDTO> reviews = Arrays.asList(new ReviewDTO(), new ReviewDTO());
        ResponseEntity<List<ReviewDTO>> responseEntity = ResponseEntity.ok(reviews);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        List<ReviewDTO> result = animeService.getReviewsForAnime(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }

    @Test
    void CreateReview_ForAnime_CreatesReview_WhenAnimeExists() {
        Anime anime = new Anime(1L, "Naruto", "Shonen", "Action", null, 220);
        when(animeRepository.findById(1L)).thenReturn(Optional.of(anime));
        ReviewDTO reviewDTO = new ReviewDTO();
        ResponseEntity<ReviewDTO> responseEntity = ResponseEntity.ok(reviewDTO);
        when(restTemplate.postForEntity(anyString(), any(), eq(ReviewDTO.class))).thenReturn(responseEntity);

        ReviewDTO result = animeService.createReviewForAnime(1L, reviewDTO);

        assertNotNull(result);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(ReviewDTO.class));
    }

    @Test
    void UpdateReview_ForAnime_UpdatesReview_WhenAnimeExists() {
        Anime anime = new Anime(1L, "Naruto", "Shonen", "Action", null, 220);
        when(animeRepository.findById(1L)).thenReturn(Optional.of(anime));
        ReviewDTO reviewDTO = new ReviewDTO();
        ResponseEntity<ReviewDTO> responseEntity = ResponseEntity.ok(reviewDTO);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(ReviewDTO.class))).thenReturn(responseEntity);

        ReviewDTO result = animeService.updateReviewForAnime(1L, 1L, reviewDTO);

        assertNotNull(result);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(ReviewDTO.class));
    }

    @Test
    void DeleteReview_ForAnime_DeletesReview_WhenAnimeExists() {
        Anime anime = new Anime(1L, "Naruto", "Shonen", "Action", null, 220);
        when(animeRepository.findById(1L)).thenReturn(Optional.of(anime));

        animeService.deleteReviewForAnime(1L, 1L);

        verify(restTemplate, times(1)).delete(anyString());
    }
}
