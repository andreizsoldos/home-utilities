package com.home.utilities.configuration.userdetails.service;

import com.home.utilities.configuration.userdetails.UserPrincipal;
import com.home.utilities.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) {
        final var user = userRepository.findByEmail(email)
              .orElseThrow(() -> new UsernameNotFoundException("No user found with this email: " + email));
        return UserPrincipal.create(user);
    }
}
