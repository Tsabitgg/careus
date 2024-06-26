package com.web.careus.service;

import com.web.careus.dto.request.LoginRequest;
import com.web.careus.dto.request.SignupRequest;
import com.web.careus.dto.response.JwtResponse;
import com.web.careus.model.user.User;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response) throws BadRequestException;
    User registerUser(SignupRequest signUpRequest) throws BadRequestException;
}
