package com.example.myelkprojct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class KafkaConfig {
    @Value("${kafka.rest.url}")
    private String kafkaRestUrl;

    public String getKafkaRestUrl() {
        return kafkaRestUrl;
    }
}
