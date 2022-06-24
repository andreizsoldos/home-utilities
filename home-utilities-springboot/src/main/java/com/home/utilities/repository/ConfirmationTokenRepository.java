package com.home.utilities.repository;

import com.home.utilities.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    ConfirmationToken findByToken(String token);

    List<ConfirmationToken> findByUserId(Long userId);
}
