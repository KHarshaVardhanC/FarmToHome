package com.ftohbackend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ImageRepository  extends JpaRepository<Image, Long> {
}