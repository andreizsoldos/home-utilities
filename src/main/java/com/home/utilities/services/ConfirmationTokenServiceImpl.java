package com.home.utilities.services;

import com.home.utilities.entities.AccountStatus;
import com.home.utilities.entities.ConfirmationToken;
import com.home.utilities.entities.User;
import com.home.utilities.exceptions.AccountActivatedException;
import com.home.utilities.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final Environment environment;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public ConfirmationToken findByToken(final String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    public Optional<ConfirmationToken> findByUserId(final Long userId) {
        return confirmationTokenRepository.findByUserId(userId);
    }

    @Override
    public ConfirmationToken save(final ConfirmationToken token) {
        return confirmationTokenRepository.save(token);
    }

    @Override
    public ConfirmationToken generateToken(final User user) {
        final var validFor = Integer.parseInt(Objects.requireNonNull(environment.getProperty("confirmation.token.validity")));
        final var confirmationToken = new ConfirmationToken(user, validFor);
        return this.save(confirmationToken);
    }

    @Override
    public Boolean validateToken(final String token) {
        final var confirmationToken = confirmationTokenRepository.findByToken(token);
        if (confirmationToken.getUser().getStatus() == AccountStatus.ACTIVE) {
            throw new AccountActivatedException("Account already activated");
        }
        return token != null &&
              confirmationToken.getExpiresAt().isAfter(Instant.now()) &&
              confirmationToken.getValid();
    }
}
