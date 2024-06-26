package com.web.careus.controller;

import com.web.careus.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    @Autowired
    private WhatsAppService whatsAppService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification() {
        String result = whatsAppService.sendMessage();
        return ResponseEntity.ok(result);
    }
}

