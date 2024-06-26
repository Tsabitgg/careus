package com.web.careus.repository;

import com.web.careus.enumeration.ETopic;
import com.web.careus.model.news.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("SELECT n FROM News n WHERE n.topic.topicName = :topicName")
    Page<News> findByTopicName(@Param("topicName") ETopic topicName, Pageable pageable);

    @Query("SELECT n FROM News n ORDER BY n.newsId DESC")
    Page<News> findAllByDescending(Pageable pageable);
    News findById(long id);
}
