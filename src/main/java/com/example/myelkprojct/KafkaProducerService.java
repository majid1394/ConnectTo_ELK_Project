package com.example.myelkprojct;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    @Autowired
    private KafkaConfig kafkaConfig;

 /*   @Autowired
    private  KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }*/

    public String scrapeAndSendToElk() {
        logger.info("Request has been sent to scrapeAndSendToElk ");
        int count = 0;

        try {
            // Scrape data from Divar
            Document doc = Jsoup.connect("https://divar.ir/s/tehran/buy-apartment").get();
            for (Element element : doc.select(".post-list__widget-col-c1444") ) { // Adjust the selector based on the actual HTML structure
                if (count<10) {
                    count++;
                    String title = element.select(".kt-post-card__title").text();
                    String description = element.select(".kt-post-card__description").text();
                    String jsonMessage = String.format("{\"title\":\"%s\", \"description\":\"%s\"}", title, description);
                    sendJsonToKafkaRest("divarApp-topic", jsonMessage);
                    //kafkaTemplate.send("divar-topic", title+description);
                }
            }
        } catch (IOException e) {
            logger.error("Error while scraping data: ", e);
            e.printStackTrace();
        }
        return "Message sent to Kafka topic: " + "divarApp-topic";
    }

    public void sendJsonToKafkaRest(String topic, String message) {

           logger.info("Title: {}, Price: {} , request has been sent to KafkaProducerService class with topic: ", topic);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = kafkaConfig.getKafkaRestUrl();
            HttpPost postRequest = new HttpPost(url);
            message = message.replace("\"", ""); // This will remove any double quotes from the message
            String jsonPayload = "{\"records\":[{\"value\":{\"key\":\"" + message + "\"}}]}";


            // Set the entity with UTF-8 encoding
            StringEntity entity = new StringEntity(jsonPayload, "UTF-8");
            entity.setContentType("application/vnd.kafka.json.v2+json; charset=UTF-8");
            postRequest.setEntity(entity);
            // Execute the request
            CloseableHttpResponse response = httpClient.execute(postRequest);
            // Handle the response (optional)
            System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
            logger.info("Response from Kafka REST proxy: " + response);

        } catch (Exception e) {
            logger.error("Error sending JSON to Kafka REST proxy: ", e);
            e.printStackTrace();
        }
    }
}

