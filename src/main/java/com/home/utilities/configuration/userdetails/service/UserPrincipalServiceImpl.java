package com.home.utilities.configuration.userdetails.service;

import com.home.utilities.configuration.logger.ServiceLogger;
import com.home.utilities.configuration.logger.ServiceLoggerFactory;
import com.home.utilities.configuration.userdetails.UserPrincipal;
import com.home.utilities.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPrincipalServiceImpl implements UserDetailsService {

    private static final ServiceLogger LOGGER = ServiceLoggerFactory.getServiceLogger(LoggerFactory.getLogger(UserPrincipalServiceImpl.class));

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) {
        final var user = userRepository.findByEmail(email)
              .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserPrincipal.create(user);
    }
}
