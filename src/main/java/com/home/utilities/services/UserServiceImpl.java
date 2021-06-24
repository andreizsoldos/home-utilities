package com.home.utilities.services;

import com.home.utilities.entities.AccountStatus;
import com.home.utilities.entities.Gender;
import com.home.utilities.entities.User;
import com.home.utilities.entities.UserRole;
import com.home.utilities.payload.request.RegisterRequest;
import com.home.utilities.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Optional<User> createAccount(final RegisterRequest request) {
        final var encodedPassword = passwordEncoder.encode(request.getPassword());
        final var user = new User(request.getEmail(), encodedPassword, request.getFirstName(), request.getLastName(),
              request.getGender(), request.getTerms(), request.getGdpr(), UserRole.USER, AccountStatus.ACTIVE);
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Boolean existsEmail(final String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<Gender> getGenderValues() {
        return List.of(Gender.values());
    }
}
