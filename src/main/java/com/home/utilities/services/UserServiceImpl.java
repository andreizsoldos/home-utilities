package com.home.utilities.services;

import com.home.utilities.entities.*;
import com.home.utilities.exceptions.AccountActivatedException;
import com.home.utilities.exceptions.NotFoundException;
import com.home.utilities.exceptions.TokenNotFoundException;
import com.home.utilities.payload.request.RegisterRequest;
import com.home.utilities.payload.request.SupportRequest;
import com.home.utilities.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
              .orElseThrow(() -> new UsernameNotFoundException("No user found with this email: " + email));
    }

    @Override
    public Optional<User> createAccount(final RegisterRequest request) {
        final var encodedPassword = passwordEncoder.encode(request.getPassword());
        final var user = new User(request.getEmail(), encodedPassword, request.getFirstName(), request.getLastName(),
              request.getGender(), request.getTerms(), request.getGdpr(), UserRole.USER, AccountStatus.LOCKED);
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

    @Override
    public User activateAccount(final String token) {
        return Optional.ofNullable(confirmationTokenService.findByToken(token))
              .map(confirmationToken -> {
                  final var user = confirmationToken.getUser();
                  user.setStatus(AccountStatus.ACTIVE);
                  userRepository.save(user);
                  confirmationToken.setValid(false);
                  confirmationTokenService.save(confirmationToken);
                  return user;
              })
              .orElseThrow(() -> new TokenNotFoundException("Token"));
    }

    @Override
    public Optional<User> checkAccount(final SupportRequest request) {
        return userRepository.findByEmail(request.getEmail())
              .map(u -> {
                  if (u.getStatus() == AccountStatus.ACTIVE) {
                      throw new AccountActivatedException("Account already activated");
                  } else if (u.getStatus() == AccountStatus.LOCKED) {
                      final var confirmationToken = confirmationTokenService.findByUserId(u.getId());
                      if (confirmationToken.isEmpty()) {
                          throw new TokenNotFoundException("Token");
                      } else {
                          confirmationToken.stream()
                                .filter(ConfirmationToken::getValid)
                                .forEach(c -> {
                                    c.setValid(false);
                                    confirmationTokenService.save(c);
                                });
                      }
                      return u;
                  } else {
                      throw new NotFoundException("Account status error");
                  }
              });
    }
}
