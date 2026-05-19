package com.example.TipsManagement.controller;

import com.example.TipsManagement.model.LoggedUser;
import com.example.TipsManagement.model.dto.Request.BetRequest;
import com.example.TipsManagement.model.dto.Request.BetStatusRequest;
import com.example.TipsManagement.model.dto.Response.BetResponse;
import com.example.TipsManagement.service.BetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bet")
public class BetController {
    private final BetService betService;

    public BetController(BetService betService) {
        this.betService = betService;
    }
    @PostMapping
    public ResponseEntity<BetResponse> saveBet(@AuthenticationPrincipal LoggedUser usuario, @RequestBody @Valid BetRequest betRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(betService.save(usuario.getId(), betRequest));
    }

    @PutMapping("/{betId}")
    public ResponseEntity<BetResponse> updateBet(@AuthenticationPrincipal LoggedUser usuario, @RequestBody @Valid BetRequest betRequest, @PathVariable Long betId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(betService.update(usuario.getId(), betId, betRequest));
    }

    @PatchMapping("/{betId}/status")
    public ResponseEntity<BetResponse> updateBetStatus(@AuthenticationPrincipal LoggedUser usuario, @RequestBody BetStatusRequest betStatusRequest, @PathVariable Long betId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(betService.updateBetStatus(usuario.getId(), betId, betStatusRequest));
    }

    @GetMapping
    public ResponseEntity<Object> listAllBet(@AuthenticationPrincipal LoggedUser usuario){
        return ResponseEntity.status(HttpStatus.OK)
                .body(betService.listAll(usuario.getId()));
    }

    @GetMapping("/pending")
    public ResponseEntity<Object> listAllPendingBet(@AuthenticationPrincipal LoggedUser usuario){
        return ResponseEntity.status(HttpStatus.OK)
                .body(betService.listAllPending(usuario.getId()));
    }
}
