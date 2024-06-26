package com.web.careus.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.careus.model.news.Topic;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class NewsRequest {
    private String title;
    private String content;
    private MultipartFile image;
    private Topic topic;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
