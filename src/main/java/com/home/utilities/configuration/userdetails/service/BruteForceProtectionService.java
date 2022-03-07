package com.home.utilities.configuration.userdetails.service;

import com.home.utilities.entities.User;

import java.util.Optional;

public interface BruteForceProtectionService {

    Optional<User> findByEmail(String email);

    void registerFailedLoginAttempts(User user);

    void resetFailedLoginAttempts(User user);

    void lockAccount(User user);

    void unlockAccount(User user);

    boolean isLockDurationExpired(User user);

    Integer getMaximumFailedAttempts();

    long getLockTimeDurationInHours();
}
