package com.example.TipsManagement.scheduler;

import com.example.TipsManagement.service.MatchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GetMatchesScheduler {
    private final MatchService matchService;

    public GetMatchesScheduler(MatchService matchService) {
        this.matchService = matchService;
    }

    @Scheduled(fixedDelay = 3600000)
    public void syncMatches() {

        System.out.println("Sincronizando partidas...");

        matchService.saveMatches();
    }
}
