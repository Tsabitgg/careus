package com.web.careus.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.web.careus.dto.request.EditProfileRequest;
import com.web.careus.model.user.User;
import com.web.careus.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getCurrentUser() throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));
        }
        throw new BadRequestException("User not found");
    }

    @Override
    public User editProfile(EditProfileRequest editProfileRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (editProfileRequest.getUsername() != null) {
                existingUser.setUsername(editProfileRequest.getUsername());
            }
            if (editProfileRequest.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(editProfileRequest.getPhoneNumber());
            }
            if (editProfileRequest.getPassword() != null) {
                existingUser.setPassword(encoder.encode(editProfileRequest.getPassword()));
            }
            if (editProfileRequest.getAddress() != null) {
                existingUser.setAddress(editProfileRequest.getAddress());
            }

            if (editProfileRequest.getProfilePicture() != null && !editProfileRequest.getProfilePicture().isEmpty()) {
                try {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(
                            editProfileRequest.getProfilePicture().getBytes(),
                            ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("url").toString();
                    existingUser.setProfilePicture(imageUrl);
                } catch (IOException e) {
                    throw new BadRequestException("Error uploading image", e);
                }
            }
            return userRepository.save(existingUser);
        }
        throw new BadRequestException("User not found");
    }
}
