package com.web.careus.controller;

import com.web.careus.dto.request.NewsRequest;
import com.web.careus.dto.response.MessageResponse;
import com.web.careus.dto.response.TopicResponse;
import com.web.careus.enumeration.ETopic;
import com.web.careus.model.news.News;
import com.web.careus.service.NewsService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping("/admin/create-news")
    public ResponseEntity<?> createNews(@ModelAttribute NewsRequest newsRequest){
        try{
            News createdNews = newsService.createNews(newsRequest);
            return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/admin/update-news/{id}")
    public ResponseEntity<?> updateNews(@PathVariable long id, @ModelAttribute NewsRequest newsRequest){
        try{
            News updatedNews = newsService.updateNews(id,newsRequest);
            return new ResponseEntity<>(updatedNews, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/admin/delete-news/{id}")
    public ResponseEntity<MessageResponse> deleteNews(@PathVariable long id) {
        try {
            newsService.deleteNews(id);
            return ResponseEntity.ok().body(new MessageResponse("Delete news successfully"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }


    @GetMapping("/news")
    public Page<News> getAllNews(@RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12; // Jumlah per halaman
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return newsService.getAllNews(pageRequest);
    }

    @GetMapping("/news/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable long id){
        Optional<News> news = newsService.getNewsById(id);
        if (news.isPresent()){
            return new ResponseEntity<>(news.get(), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/news/topic")
    public Page<News> getNewsByTopic(@RequestParam String topicName,
                                     @RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12; // Jumlah campaign per halaman
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return newsService.getNewsByTopicName(topicName, pageRequest);
    }

    @GetMapping("/news/all-topic")
    public List<TopicResponse> getAllNewsTopic() {
        ETopic[] topic = ETopic.values();
        List<TopicResponse> newsTopicResponse = new ArrayList<>();

        for (int i = 0; i < topic.length; i++) {
            TopicResponse campaignCategoryDTO = new TopicResponse();
            campaignCategoryDTO.setId(i + 1);
            campaignCategoryDTO.setTopicName(topic[i].toString());
            newsTopicResponse.add(campaignCategoryDTO);
        }
        return newsTopicResponse;
    }
}
