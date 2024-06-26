package com.web.careus.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.web.careus.service.CloudinaryService;
import com.web.careus.service.CloudinaryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary (ObjectUtils.asMap(
                "cloud_name", "donation-application",
                "api_key", "931245915128513",
                "api_secret", "W4-2zNnA8R3spiSaM1LNBq53bNg"));
    }
    @Bean
    public CloudinaryService cloudinaryService(){
        return new CloudinaryServiceImpl(cloudinary());
    }
}
