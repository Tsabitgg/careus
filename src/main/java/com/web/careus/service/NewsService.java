package com.web.careus.service;

import com.web.careus.dto.request.NewsRequest;
import com.web.careus.model.news.News;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NewsService {
    News createNews(NewsRequest newsRequest) throws BadRequestException;
    News updateNews(long id, NewsRequest newsRequest) throws BadRequestException;
    void deleteNews(long id) throws BadRequestException;
    Page<News> getAllNews(Pageable pageable);
    Optional<News> getNewsById(long id);
    Page<News> getNewsByTopicName(String topicName, Pageable pageable);
}
