package com.web.careus.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.web.careus.dto.request.NewsRequest;
import com.web.careus.enumeration.ERole;
import com.web.careus.enumeration.ETopic;
import com.web.careus.model.news.News;
import com.web.careus.model.news.Topic;
import com.web.careus.model.user.User;
import com.web.careus.repository.NewsRepository;
import com.web.careus.repository.TopicRepository;
import com.web.careus.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService{

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public News createNews(NewsRequest newsRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (!existingUser.getRole().getName().equals(ERole.ADMIN)) {
                throw new BadRequestException("Only ADMIN users can create campaigns");
            }

            Topic topic = topicRepository.findById(newsRequest.getTopic().getTopicId())
                    .orElseThrow(() -> new BadRequestException("Topic not found"));
            News news = modelMapper.map(newsRequest, News.class);
            news.setTopic(topic);

            if (newsRequest.getContent() == null || newsRequest.getContent().isEmpty()) {
                throw new BadRequestException("Content cannot be empty");
            }

            if (newsRequest.getImage() != null && !newsRequest.getImage().isEmpty()) {
                try {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(
                            newsRequest.getImage().getBytes(),
                            ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("url").toString();
                    news.setImage(imageUrl);

                } catch (IOException e) {
                    throw new BadRequestException("Error uploading image", e);
                }
            }

            return newsRepository.save(news);
        }
        throw new BadRequestException("Admin not found");
    }

    @Override
    public News updateNews(long id, NewsRequest newsRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (!existingUser.getRole().getName().equals(ERole.ADMIN)) {
                throw new BadRequestException("Only ADMIN users can create campaigns");
            }

            News updatedNews = newsRepository.findById(id);
            if (updatedNews != null) {
                updatedNews.setTitle(newsRequest.getTitle());
                updatedNews.setContent(newsRequest.getContent());
                updatedNews.setDate(newsRequest.getDate());

                Topic topic = topicRepository.findById(newsRequest.getTopic().getTopicId()).orElseThrow(() -> new BadRequestException("Topic not found"));
                updatedNews.setTopic(topic);

                if (newsRequest.getImage() != null && !newsRequest.getImage().isEmpty()) {
                    try {
                        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                                newsRequest.getImage().getBytes(),
                                ObjectUtils.emptyMap());
                        String imageUrl = uploadResult.get("url").toString();
                        updatedNews.setImage(imageUrl);

                    } catch (IOException e) {
                        throw new BadRequestException("Error uploading image", e);
                    }
                }
            }
            return newsRepository.save(updatedNews);
        }
        throw new BadRequestException("Admin not found");
    }

    @Override
    public void deleteNews(long id) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (!existingUser.getRole().getName().equals(ERole.ADMIN)) {
                throw new BadRequestException("Only ADMIN users can create campaigns");
            }

            News deletedNews = newsRepository.findById(id);
            if (deletedNews == null) {
                throw new BadRequestException("News not found");
            }
            newsRepository.delete(deletedNews);
        }
        throw new BadRequestException("Admin not found");
    }


    @Override
    public Page<News> getAllNews(Pageable pageable) {
        return newsRepository.findAllByDescending(pageable);
    }

    @Override
    public Optional<News> getNewsById(long id) {
        return Optional.ofNullable(newsRepository.findById(id));
    }

    @Override
    public Page<News> getNewsByTopicName(String topicName, Pageable pageable) {
        return newsRepository.findByTopicName(ETopic.valueOf(topicName.toUpperCase()), pageable);
    }
}
