package com.home.utilities.repository;

import com.home.utilities.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    ConfirmationToken findByToken(String token);

    Optional<ConfirmationToken> findByUserId(Long userId);
}
