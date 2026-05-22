package com.example.TipsManagement.mapper;

import com.example.TipsManagement.model.Match;
import com.example.TipsManagement.model.dto.MatchDTO;
import com.example.TipsManagement.model.dto.OpponentDTO;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    public Match toEntity(MatchDTO matchDTO){
        //somente quero salvar jogos que já possuem os 2 oponentes definidos
        if(matchDTO.getOpponents() == null || matchDTO.getOpponents().size() < 2) {
            return null;
        }
        Match match = new Match();

        match.setPandaScoreId(matchDTO.getId());
        if(matchDTO.getVideogame() != null) {
            match.setGame(matchDTO.getVideogame().getName());
        }
        match.setMatchDate(matchDTO.getBegin_at());

        OpponentDTO home = matchDTO.getOpponents().get(0).getOpponent();
        OpponentDTO away = matchDTO.getOpponents().get(1).getOpponent();

        match.setHomeTeam(home.getName());
        match.setAwayTeam(away.getName());

        if(home.getImage_url() != null && !home.getImage_url().isBlank()) {
            match.setHomeTeamImage(home.getImage_url());
        }
        if(away.getImage_url() != null && !away.getImage_url().isBlank()) {
            match.setAwayTeamImage(away.getImage_url());
        }

        return match;
    }
}
