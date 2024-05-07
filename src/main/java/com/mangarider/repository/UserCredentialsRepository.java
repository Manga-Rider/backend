package com.mangarider.repository;

import com.mangarider.model.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, UUID> {
    Optional<UserCredentials> findByEmail(String email);

    @Query("select (count(u) > 0) from UserCredentials u where u.email = ?1")
    boolean existsByEmail(String email);
}
