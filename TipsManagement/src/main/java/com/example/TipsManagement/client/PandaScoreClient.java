package com.example.TipsManagement.client;

import com.example.TipsManagement.model.dto.MatchDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class PandaScoreClient {

    private final WebClient webClient;
    private final String token;

    public PandaScoreClient(
            @Qualifier("pandaScoreWebClient") WebClient webClient,
            @Value("${pandascore.token}") String token
    ) {
        this.webClient = webClient;
        this.token = token;

        System.out.println("TOKEN: " + token);
    }

    public List<MatchDTO> getMatches() {

        return webClient.get()
                .uri("/matches?token=" + token)
                .retrieve()
                .bodyToFlux(MatchDTO.class)
                .collectList()
                .block();
    }
}
