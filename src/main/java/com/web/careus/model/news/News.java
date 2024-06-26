package com.web.careus.model.news;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long newsId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;
    private String image;

    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "topicId")
    private Topic topic;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
