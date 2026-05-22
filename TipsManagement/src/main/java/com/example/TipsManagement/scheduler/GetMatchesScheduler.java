package com.example.TipsManagement.scheduler;

import com.example.TipsManagement.service.PandaScoreService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GetMatchesScheduler {
    private final PandaScoreService pandaScoreService;

    public GetMatchesScheduler(PandaScoreService pandaScoreService) {
        this.pandaScoreService = pandaScoreService;
    }

    @Scheduled(fixedDelay = 3600000)
    public void syncMatches() {

        System.out.println("Sincronizando partidas...");

        pandaScoreService.savePandaScoreMatches();
    }
}
