package com.example.myelkprojct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;

@RestController
public class MessageController {

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public MessageController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String topic, @RequestParam String message) {
        kafkaProducerService.sendMessage(topic, message);
        return "Message sent to Kafka topic: " + topic;
    }




    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @GetMapping("/hello")
    public String hello() {
        logger.info("Hello endpoint was called");
        return "Hello, " +
                "" +
                "" +
                "World!";
    }





    @GetMapping("/scrape")
    public void scrapeAndSendToElk() {

        int count=0;
        try {
            // Scrape data from Divar
            Document doc = Jsoup.connect("https://divar.ir/s/tehran/buy-apartment").get();
            for (Element element : doc.select(".post-list__widget-col-c1444") ) { // Adjust the selector based on the actual HTML structure
                if (count<10) {
                    count++;
                    String title = element.select(".kt-post-card__title").text();
                    String description = element.select(".kt-post-card__description").text();
                    //String link = element.select("a").attr("href");

                    // Send data to Kafka (which Logstash can read from)
                    //String message = String.format("{\"title\": \"%s\", \"price\": \"%s\"}", title, description);
                    //kafkaTemplate.send("divar-topic", message);
                    // Process the data as needed
                    sendToLogstash(title, description);

                    // Create a data object
                    //            Post post = new Post(title, description);

                    // Send to Elasticsearch
                    //          sendToElasticsearch(post);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   // @Scheduled(fixedRate = 60000) // Scrape every minute
    private void sendToLogstash(String title, String description) {
        logger.info("Title: {}, Price: {}", title,description);
        //logger.info("Title: {}, Price: {}", title, price);

        //String url = "http://10.2.35.160:5044/posts/_doc"; // Adjust the URL based on your Elasticsearch setup
        //restTemplate.postForObject(url, post, String.class);
    }

}