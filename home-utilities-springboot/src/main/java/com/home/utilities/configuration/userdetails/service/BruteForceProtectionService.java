package com.home.utilities.configuration.userdetails.service;

import com.home.utilities.entity.IpInfo;
import com.home.utilities.entity.User;

import java.util.Optional;

public interface BruteForceProtectionService {

    Optional<User> findByEmail(String email);

    Optional<IpInfo> findByIp(String ipAddress);

    void registerUserFailedLoginAttempts(User user);

    void resetUserFailedLoginAttempts(User user);

    void registerIpFailedLoginAttempts(IpInfo ipInfo);

    void resetIpFailedLoginAttempts(IpInfo ipInfo);

    void lockUserAccount(User user);

    void lockIp(IpInfo ipInfo);

    void unlockUserAccount(User user);

    void unlockIp(IpInfo ipInfo);

    boolean isUserLockDurationExpired(User user);

    boolean isIpLockDurationExpired(IpInfo ipInfo);

    Integer getMaximumUserFailedAttempts();

    Integer getMaximumIpFailedAttempts();

    void incrementTotalIpAttempts(IpInfo ipInfo);
}
