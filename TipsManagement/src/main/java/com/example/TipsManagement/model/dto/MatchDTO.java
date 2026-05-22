package com.example.TipsManagement.model.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class MatchDTO {

    private Long id;

    private OffsetDateTime begin_at;

    private VideogameDTO videogame;

    private List<MatchOpponentDTO> opponents;


}
