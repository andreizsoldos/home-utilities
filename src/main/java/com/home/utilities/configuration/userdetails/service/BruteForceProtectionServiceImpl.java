package com.home.utilities.configuration.userdetails.service;

import com.home.utilities.entities.AccountStatus;
import com.home.utilities.entities.User;
import com.home.utilities.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BruteForceProtectionServiceImpl implements BruteForceProtectionService {

    private final UserRepository userRepository;

    @Value("${application.security.failed.login.count.max}")
    private int maxFailedLogins;

    @Value("${application.security.failed.login.lock.duration.hours}")
    private long lockTimeDuration;

    @Override
    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void registerFailedLoginAttempts(final User user) {
        final int failedCounter = user.getFailedLoginAttempts();
        if (failedCounter < maxFailedLogins) {
            user.setFailedLoginAttempts(failedCounter + 1);
            userRepository.save(user);
        }
    }

    @Override
    public void resetFailedLoginAttempts(final User user) {
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    @Override
    public void lockAccount(final User user) {
        user.setStatus(AccountStatus.LOCKED);
        user.setEndLockTime(LocalDateTime.now(ZoneId.systemDefault())
              .plusHours(lockTimeDuration));
        userRepository.save(user);
    }

    @Override
    public void unlockAccount(final User user) {
        user.setStatus(AccountStatus.ACTIVE);
        user.setFailedLoginAttempts(0);
        user.setEndLockTime(null);
        userRepository.save(user);
    }

    @Override
    public boolean isLockDurationExpired(final User user) {
        final var lockTime = user.getEndLockTime();
        final var currentTime = LocalDateTime.now(ZoneId.systemDefault());
        return currentTime.isAfter(lockTime);
    }

    @Override
    public Integer getMaximumFailedAttempts() {
        return maxFailedLogins;
    }

    @Override
    public long getLockTimeDurationInHours() {
        return lockTimeDuration;
    }
}
