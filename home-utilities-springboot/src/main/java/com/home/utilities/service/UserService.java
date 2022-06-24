package com.home.utilities.service;

import com.home.utilities.entity.Gender;
import com.home.utilities.entity.User;
import com.home.utilities.payload.request.RegisterRequest;
import com.home.utilities.payload.request.SupportRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);

    Optional<User> createAccount(RegisterRequest request);

    Boolean existsEmail(String email);

    List<Gender> getGenderValues();

    User activateAccount(String token);

    Optional<User> checkAccount(SupportRequest request);
}
