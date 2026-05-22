package com.example.TipsManagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class config {
    @Configuration
    public static class WebClientConfig {

        @Bean
        public WebClient pandaScoreWebClient() {
            return WebClient.builder()
                    .baseUrl("https://api.pandascore.co")
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .build();
        }
    }
}
