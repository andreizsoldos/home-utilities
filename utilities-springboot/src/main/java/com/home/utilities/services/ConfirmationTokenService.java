package com.home.utilities.services;

import com.home.utilities.entities.ConfirmationToken;
import com.home.utilities.entities.User;

import java.util.List;

public interface ConfirmationTokenService {

    ConfirmationToken findByToken(String token);

    List<ConfirmationToken> findByUserId(Long userId);

    ConfirmationToken save(ConfirmationToken token);

    ConfirmationToken generateToken(User user);

    Boolean validateToken(String token);
}
