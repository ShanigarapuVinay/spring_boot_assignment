package com.vinay.spring_boot_assignment.anime_service.util;

public class Constants {
    private Constants() {
        throw new UnsupportedOperationException("This is a util class and cannot be instantiated");
    }
    public static final String ANIME_BASE_URL = "/anime";
    public static final String ANIME_ID = "/{id}";
    public static final String ANIME_ID_REVIEWS = "/{animeId}/reviews";
    public static final String ANIME_ID_REVIEWS_REVIEW_ID = "/{animeId}/reviews/{reviewId}";
}