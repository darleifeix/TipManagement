package com.example.TipsManagement.service;

import com.example.TipsManagement.Exception.NotFoundException;
import com.example.TipsManagement.mapper.BancaMapper;
import com.example.TipsManagement.model.Banca;
import com.example.TipsManagement.model.BetHouse;
import com.example.TipsManagement.model.dto.Request.BancaRequest;
import com.example.TipsManagement.model.dto.Response.BancaResponse;
import com.example.TipsManagement.repository.IBancaRepository;
import com.example.TipsManagement.repository.IBetHouseRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BancaService {
    private final IBancaRepository bancaRepository;
    private final IBetHouseRepository betHouseRepository;
    private final BancaMapper bancaMapper;

    public BancaService(IBancaRepository bancaRepository, IBetHouseRepository betHouseRepository, BancaMapper bancaMapper) {
        this.bancaRepository = bancaRepository;
        this.betHouseRepository = betHouseRepository;
        this.bancaMapper = bancaMapper;
    }

    public BancaResponse save(Long userId, Long betHouseId, BancaRequest bancaRequest) {

        BetHouse betHouse = betHouseRepository
                .findByIdAndUsuarioId(betHouseId, userId)
                .orElseThrow(() -> new NotFoundException("Casa de aposta não encontrada"));

        Banca banca = new Banca();
        banca.setBalance(bancaRequest.getAmount());
        banca.setBetHouse(betHouse);
        Banca savedBanca = bancaRepository.save(banca);

        return bancaMapper.toResponse(savedBanca);
    }

    public List<BancaResponse> listAll(Long userId){
        List<Banca> list = bancaRepository.findAllByBetHouse_UsuarioId(userId);
        if(list.isEmpty()){
            throw new NotFoundException("Não existe Banca cadastrada.");
        }

        //Retorna a lista convertida para BancaResponse, evitando passar dados sensíveis do usuario cadastrado (senha)
        return list.stream()
                .map(bancaMapper::toResponse)
                .toList();
    }

    public BancaResponse list(Long userId, Long id){
        Banca banca = getOwnedBanca(userId, id);

        return bancaMapper.toResponse(banca);
    }

    //metodo para validar se a banca existe, vai ser usado em todas transações/Bets
    public Banca getOwnedBanca(Long userId,Long bancaId) {
        return bancaRepository.findByIdAndBetHouse_UsuarioId(bancaId, userId)
                .orElseThrow(() ->
                        new NotFoundException("Não existe Banca com os parâmetros fornecidos."));
    }
}
