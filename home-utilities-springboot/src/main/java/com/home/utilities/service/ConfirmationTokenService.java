package com.home.utilities.service;

import com.home.utilities.entity.ConfirmationToken;
import com.home.utilities.entity.User;

import java.util.List;

public interface ConfirmationTokenService {

    ConfirmationToken findByToken(String token);

    List<ConfirmationToken> findByUserId(Long userId);

    ConfirmationToken save(ConfirmationToken token);

    ConfirmationToken generateToken(User user);

    Boolean validateToken(String token);
}
