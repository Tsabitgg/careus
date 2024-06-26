package com.web.careus.controller;

import com.web.careus.dto.request.LoginRequest;
import com.web.careus.dto.request.SignupRequest;
import com.web.careus.dto.response.JwtResponse;
import com.web.careus.dto.response.MessageResponse;
import com.web.careus.model.user.User;
import com.web.careus.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) throws BadRequestException {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest, response);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws BadRequestException {
        User user = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public MessageResponse logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        return new MessageResponse("Logout Successfully");
    }
}
