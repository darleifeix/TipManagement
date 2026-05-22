package com.example.TipsManagement.service;

import com.example.TipsManagement.client.PandaScoreClient;
import com.example.TipsManagement.mapper.MatchMapper;
import com.example.TipsManagement.model.Match;
import com.example.TipsManagement.repository.IMatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MatchService {

    private final IMatchRepository matchRepository;
    private final PandaScoreClient pandaScoreClient;
    private final MatchMapper matchMapper;

    public MatchService(IMatchRepository matchRepository, PandaScoreClient pandaScoreClient, MatchMapper matchMapper) {
        this.matchRepository = matchRepository;
        this.pandaScoreClient = pandaScoreClient;
        this.matchMapper = matchMapper;
    }

    public void saveMatches() {

        List<Match> matches = pandaScoreClient.getMatches()
                .stream()
                .map(matchMapper::toEntity)
                .filter(Objects::nonNull)
                .toList();

        matchRepository.saveAll(matches);

        System.out.println("Partidas salvas: " + matches.size());
    }

    public List<Match> listAllMatches(){
        return matchRepository.findAll();
    }

}
