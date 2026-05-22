package com.example.TipsManagement.service;

import com.example.TipsManagement.client.PandaScoreClient;
import com.example.TipsManagement.mapper.MatchMapper;
import com.example.TipsManagement.model.Match;
import com.example.TipsManagement.model.dto.MatchDTO;
import com.example.TipsManagement.repository.IMatchRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PandaScoreService {

    private final IMatchRepository matchRepository;
    private final PandaScoreClient pandaScoreClient;
    private final MatchMapper matchMapper;

    public PandaScoreService(IMatchRepository matchRepository, PandaScoreClient pandaScoreClient, MatchMapper matchMapper) {
        this.matchRepository = matchRepository;
        this.pandaScoreClient = pandaScoreClient;
        this.matchMapper = matchMapper;
    }
    @PostConstruct
    public void getMatchesOnStart() {
        savePandaScoreMatches();
    }

    public void savePandaScoreMatches() {
        List<MatchDTO> matches = new ArrayList<>();
        matches.addAll(pandaScoreClient.getCsMatches());
        matches.addAll(pandaScoreClient.getLolMatches());
        matches.addAll(pandaScoreClient.getValorantMatches());
        matches.addAll(pandaScoreClient.getDotaMatches());

        List<Match> convertedMatches = matches.stream()
                .map(matchMapper::toEntity)
                .filter(Objects::nonNull)
                .toList();

        matchRepository.saveAll(convertedMatches);

        System.out.println("Partidas salvas: " + convertedMatches.size());
    }

    public List<Match> listAllMatches(){
        return matchRepository.findAll();
    }

}
