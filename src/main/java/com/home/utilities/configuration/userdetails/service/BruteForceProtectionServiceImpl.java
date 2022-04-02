package com.home.utilities.configuration.userdetails.service;

import com.home.utilities.entities.AccountStatus;
import com.home.utilities.entities.IpInfo;
import com.home.utilities.entities.User;
import com.home.utilities.repository.IpInfoRepository;
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
    private final IpInfoRepository ipInfoRepository;

    @Value("${application.security.failed.user.login.count.max}")
    private int maxUserFailedLogins;

    @Value("${application.security.failed.user.login.lock.duration.hours}")
    private long userLockTimeDuration;

    @Value("${application.security.failed.ip.login.count.max}")
    private int maxIpFailedLogins;

    @Value("${application.security.failed.ip.login.lock.duration.hours}")
    private long ipLockTimeDuration;

    @Override
    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<IpInfo> findByIp(final String ipAddress) {
        return ipInfoRepository.findByIp(ipAddress);
    }

    @Override
    public void registerUserFailedLoginAttempts(final User user) {
        final int failedCounter = user.getFailedLoginAttempts();
        if (failedCounter < maxUserFailedLogins) {
            user.setFailedLoginAttempts(failedCounter + 1);
            userRepository.save(user);
        }
    }

    @Override
    public void resetUserFailedLoginAttempts(final User user) {
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    @Override
    public void registerIpFailedLoginAttempts(final IpInfo ipInfo) {
        final int failedCounter = ipInfo.getFailedIpAttempts();
        if (failedCounter < maxIpFailedLogins) {
            ipInfo.setFailedIpAttempts(failedCounter + 1);
            ipInfoRepository.save(ipInfo);
        }
    }

    @Override
    public void resetIpFailedLoginAttempts(final IpInfo ipInfo) {
        ipInfo.setFailedIpAttempts(0);
        ipInfoRepository.save(ipInfo);
    }

    @Override
    public void lockUserAccount(final User user) {
        user.setStatus(AccountStatus.LOCKED);
        user.setEndLockTime(LocalDateTime.now(ZoneId.systemDefault())
              .plusHours(userLockTimeDuration));
        userRepository.save(user);
    }

    @Override
    public void lockIp(final IpInfo ipInfo) {
        ipInfo.setEndLockTime(LocalDateTime.now(ZoneId.systemDefault())
              .plusHours(ipLockTimeDuration));
        ipInfoRepository.save(ipInfo);
    }

    @Override
    public void unlockUserAccount(final User user) {
        user.setStatus(AccountStatus.ACTIVE);
        user.setFailedLoginAttempts(0);
        user.setEndLockTime(null);
        userRepository.save(user);
    }

    @Override
    public void unlockIp(final IpInfo ipInfo) {
        ipInfo.setFailedIpAttempts(0);
        ipInfo.setEndLockTime(null);
        ipInfoRepository.save(ipInfo);
    }

    @Override
    public boolean isUserLockDurationExpired(final User user) {
        final var lockTime = user.getEndLockTime();
        final var currentTime = LocalDateTime.now(ZoneId.systemDefault());
        return (lockTime != null && currentTime.isAfter(lockTime));
    }

    @Override
    public boolean isIpLockDurationExpired(final IpInfo ipInfo) {
        final var lockTime = ipInfo.getEndLockTime();
        final var currentTime = LocalDateTime.now(ZoneId.systemDefault());
        return (lockTime != null && currentTime.isAfter(lockTime));
    }

    @Override
    public Integer getMaximumUserFailedAttempts() {
        return maxUserFailedLogins;
    }

    @Override
    public Integer getMaximumIpFailedAttempts() {
        return maxIpFailedLogins;
    }

    @Override
    public void incrementTotalIpAttempts(final IpInfo ipInfo) {
        final long ipAttempts = ipInfo.getTotalIpAttempts();
        ipInfo.setTotalIpAttempts(ipAttempts + 1);
        ipInfoRepository.save(ipInfo);
    }
}
