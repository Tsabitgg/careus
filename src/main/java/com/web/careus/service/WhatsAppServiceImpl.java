package com.web.careus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppServiceImpl implements WhatsAppService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String sendMessage() {
        String url = "https://wa.srv11.wapanels.com/send-message";

        Map<String, String> request = new HashMap<>();
        request.put("api_key", "98057717bd2cec21101df07126a1681f8e31ef13");
        request.put("sender", "6283129363915");
        request.put("number", "62895422905255");
        request.put("message", "Terimakasih telah berdonasi melalui platform lazismu.org");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }
}

