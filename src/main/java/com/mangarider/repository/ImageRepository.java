package com.mangarider.repository;

import com.mangarider.model.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<UserImage, UUID> {
}
