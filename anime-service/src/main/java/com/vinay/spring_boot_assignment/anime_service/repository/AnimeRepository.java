package com.vinay.spring_boot_assignment.anime_service.repository;

import com.vinay.spring_boot_assignment.anime_service.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
}
