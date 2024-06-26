package com.web.careus.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EditProfileRequest {
    private String username;
    private String phoneNumber;
    private String password;
    private MultipartFile profilePicture;
    private String address;
}
