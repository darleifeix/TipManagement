package com.example.TipsManagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    private Long pandaScoreId;

    private String game;

    private String homeTeam;

    private String awayTeam;

    private String homeTeamImage;

    private String awayTeamImage;

    private OffsetDateTime matchDate;

}
