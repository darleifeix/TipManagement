package com.example.TipsManagement.scheduler;

import com.example.TipsManagement.service.MatchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeleteMatchScheduler {

    private final MatchService matchService;

    public DeleteMatchScheduler(MatchService matchService) {
        this.matchService = matchService;
    }

    //agendado para executar diariamente
    @Scheduled(fixedDelay = 86400000)
    public void deleteOldMatches(){
        matchService.deleteOldMatches();
    }
}
