package com.web.careus.service;

import com.web.careus.dto.request.EditProfileRequest;
import com.web.careus.model.user.User;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUser();
    Optional<User> findById(Long id);

    User getCurrentUser() throws BadRequestException;

    User editProfile (EditProfileRequest editProfileRequest) throws BadRequestException;
}
