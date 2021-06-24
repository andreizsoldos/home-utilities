package com.home.utilities.services;

import com.home.utilities.entities.Gender;
import com.home.utilities.entities.User;
import com.home.utilities.payload.request.RegisterRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> createAccount(RegisterRequest request);

    Boolean existsEmail(String email);

    List<Gender> getGenderValues();

}
