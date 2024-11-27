

package com.example.myelkprojct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;

@Controller
@RequestMapping("/api/kafka")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final KafkaProducerService kafkaProducerService;

    public MessageController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }


    // @Scheduled(fixedRate = 60000) // Scrape every minute
    @GetMapping("/scrape")
    public String scrapeAndSendToElk() {
       logger.info("request has been sent to scrape controller");
        String response = kafkaProducerService.scrapeAndSendToElk();
        return response;
    }

}