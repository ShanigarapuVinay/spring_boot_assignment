package com.vinay.spring_boot_assignment.anime_service.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private String content;
    private Double rating;
    private Long animeId;
}
