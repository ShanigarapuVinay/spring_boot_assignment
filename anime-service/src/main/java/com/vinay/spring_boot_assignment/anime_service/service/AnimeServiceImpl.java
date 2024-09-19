package com.vinay.spring_boot_assignment.anime_service.service;

import com.vinay.spring_boot_assignment.anime_service.dto.ReviewDTO;
import com.vinay.spring_boot_assignment.anime_service.model.Anime;
import com.vinay.spring_boot_assignment.anime_service.repository.AnimeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AnimeServiceImpl implements AnimeService{

    private final AnimeRepository animeRepository;
    private final RestTemplate restTemplate;
    private final String reviewServiceUrl;

    public AnimeServiceImpl(AnimeRepository animeRepository, RestTemplate restTemplate,
                            @Value("${review.service.url}") String reviewServiceUrl) {
        this.animeRepository = animeRepository;
        this.restTemplate = restTemplate;
        this.reviewServiceUrl = reviewServiceUrl;
    }

    @Override
    public List<Anime> findAll() {
        return animeRepository.findAll();
    }

    @Override
    public Optional<Anime> findById(Long id) {
        return animeRepository.findById(id);
    }

    @Override
    public Anime save(Anime anime) {
        return animeRepository.save(anime);
    }

    @Override
    public Optional<Anime> update(Long id, Anime animeDetails) {
        return animeRepository.findById(id)
                .map(anime -> {
                    anime.setTitle(animeDetails.getTitle());
                    anime.setDescription(animeDetails.getDescription());
                    anime.setGenre(animeDetails.getGenre());
                    anime.setReleaseDate(animeDetails.getReleaseDate());
                    anime.setEpisodes(animeDetails.getEpisodes());
                    return animeRepository.save(anime);
                });
    }

    @Override
    public boolean delete(Long id) {
        return animeRepository.findById(id)
                .map(anime -> {
                    // Delete reviews related to the anime
                    restTemplate.delete(reviewServiceUrl + "/reviews/anime/" + id);

                    // Now delete the anime
                    animeRepository.delete(anime);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<ReviewDTO> getReviewsForAnime(Long animeId) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new RuntimeException("Anime not found"));

        ResponseEntity<List<ReviewDTO>> response = restTemplate.exchange(
                reviewServiceUrl + "/reviews/anime/" + animeId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReviewDTO>>() {
                }
        );
        return response.getBody();
    }

    @Override
    public ReviewDTO createReviewForAnime(Long animeId, ReviewDTO reviewDTO) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new RuntimeException("Anime not found"));
        reviewDTO.setAnimeId(animeId);
        ResponseEntity<ReviewDTO> response = restTemplate.postForEntity(
                reviewServiceUrl + "/reviews",
                reviewDTO,
                ReviewDTO.class
        );
        return response.getBody();
    }

    @Override
    public ReviewDTO updateReviewForAnime(Long animeId, Long reviewId, ReviewDTO reviewDTO) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new RuntimeException("Anime not found"));
        reviewDTO.setAnimeId(animeId);

        ResponseEntity<ReviewDTO> response = restTemplate.exchange(
                reviewServiceUrl + "/reviews/" + reviewId,
                HttpMethod.PUT,
                new HttpEntity<>(reviewDTO),
                ReviewDTO.class
        );
        return response.getBody();
    }

    @Override
    public void deleteReviewForAnime(Long animeId, Long reviewId) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new RuntimeException("Anime not found"));
        restTemplate.delete(reviewServiceUrl + "/reviews/" + reviewId);
    }

}
