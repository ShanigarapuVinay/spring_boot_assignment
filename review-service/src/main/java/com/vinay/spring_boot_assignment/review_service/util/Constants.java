package com.vinay.spring_boot_assignment.review_service.util;

public class Constants {
    private Constants() {
        throw new UnsupportedOperationException("This is a util class and cannot be instantiated");
    }
    public static final String REVIEW_BASE_URL = "/reviews";
    public static final String REVIEW_ID = "/{id}";
    public static final String ANIME_ANIME_ID = "/anime/{animeId}";
}