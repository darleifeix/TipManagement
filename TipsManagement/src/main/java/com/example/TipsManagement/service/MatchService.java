package com.example.TipsManagement.service;

import com.example.TipsManagement.repository.IMatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class MatchService {

    private final IMatchRepository matchRepository;

    public MatchService(IMatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Transactional
    public void deleteOldMatches(){
        OffsetDateTime date = OffsetDateTime.now().minusDays(7);
        System.out.println(matchRepository.findByMatchDateBefore(date).size() + " partidas removidas");
        matchRepository.deleteByMatchDateBefore(date);
    }
}
