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

    }
    //metodo reutilizavel de requisição da API que recebe a rota a ser utilizada
    private List<MatchDTO> getMatches(String uri) {

        return webClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(MatchDTO.class)
                .collectList()
                .block();
    }

    //metodos que realizam as requisições separados por tipo de jogo
    public List<MatchDTO> getCsMatches() {
        return getMatches("/csgo/matches?page=1&per_page=100");
    }

    public List<MatchDTO> getLolMatches() {
        return getMatches("/lol/matches?page=1&per_page=100");
    }

    public List<MatchDTO> getValorantMatches() {
        return getMatches("/valorant/matches?page=1&per_page=100");
    }

    public List<MatchDTO> getDotaMatches() {
        return getMatches("/dota2/matches?page=1&per_page=100");
    }

}
