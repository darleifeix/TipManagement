package com.example.TipsManagement.controller;

import com.example.TipsManagement.service.PandaScoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final PandaScoreService pandaScoreService;

    public MatchController(PandaScoreService pandaScoreService) {
        this.pandaScoreService = pandaScoreService;
    }

    @GetMapping
    public ResponseEntity<Object> listMatches(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(pandaScoreService.listAllMatches());
    }
}
