package com.example.myelkprojct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Value("${kafka.rest.url}")
    private String kafkaRestUrl;

    public String getKafkaRestUrl() {
        return kafkaRestUrl;
    }
}
