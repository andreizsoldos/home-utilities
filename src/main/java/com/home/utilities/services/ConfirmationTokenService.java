package com.home.utilities.services;

import com.home.utilities.entities.ConfirmationToken;
import com.home.utilities.entities.User;

public interface ConfirmationTokenService {

    ConfirmationToken findByToken(String token);

    ConfirmationToken save(ConfirmationToken token);

    ConfirmationToken generateToken(User user);

    Boolean validateToken(String token);

    Boolean sendEmailWithToken(String email, String receiverName, String subject, String body, String attachmentFilePath, String token);
}
