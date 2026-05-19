package com.example.TipsManagement.service;

import com.example.TipsManagement.Exception.BusinessException;
import com.example.TipsManagement.Exception.NotFoundException;
import com.example.TipsManagement.mapper.BetMapper;
import com.example.TipsManagement.model.Banca;
import com.example.TipsManagement.model.Bet;
import com.example.TipsManagement.model.Tipster;
import com.example.TipsManagement.model.Usuario;
import com.example.TipsManagement.model.dto.Request.BetRequest;
import com.example.TipsManagement.model.dto.Request.BetStatusRequest;
import com.example.TipsManagement.model.dto.Response.BetResponse;
import com.example.TipsManagement.model.enums.Status;
import com.example.TipsManagement.repository.IBetRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class BetService {

    private final IBetRepository betRepository;
    private final BancaService bancaService;
    private final TipsterService tipsterService;
    private final UnitValueService unitValueService;
    private final BetMapper betMapper;
    private final TransactionService transactionService;

    public BetService(IBetRepository betRepository, TransactionService transactionService, BancaService bancaService, TipsterService tipsterService, UnitValueService unitValueService, BetMapper betMapper, TransactionService transactionService1) {
        this.betRepository = betRepository;
        this.bancaService = bancaService;
        this.tipsterService = tipsterService;
        this.unitValueService = unitValueService;
        this.betMapper = betMapper;
        this.transactionService = transactionService1;
    }

    public BetResponse save(Long userId, BetRequest betRequest){
        Banca betBanca = bancaService.getOwnedBanca(userId, betRequest.getBancaId());
        Tipster tipster = tipsterService.getOwnedTipster(userId,betRequest.getTipsterId());
        Bet bet = createBet(userId, betBanca, tipster, betRequest);
        betRepository.save(bet);
        processBetFinancials(bet);
        return betMapper.toResponse(bet);
    }

    private Bet createBet(Long userId, Banca banca, Tipster tipster, BetRequest betRequest){
        Bet bet = new Bet();

        Usuario usuario = new Usuario();
        usuario.setId(userId);
        bet.setUsuario(usuario);


        bet.setBanca(banca);
        bet.setTipster(tipster);
        bet.setSport(betRequest.getSport());
        bet.setOdd(betRequest.getOdd());
        //se status recebido for null define como pendente
        Status status = betRequest.getStatus() != null
                ? betRequest.getStatus()
                : Status.PENDING;

        bet.setStatus(status);

        bet.setHomeTeam(betRequest.getHomeTeam());
        bet.setAwayTeam(betRequest.getAwayTeam());
        bet.setDescription(betRequest.getDescription());
        bet.setMatchDate(betRequest.getMatchDate());
        bet.setUnit(betRequest.getUnit());
        bet.setUnitValue(unitValueService.getCurrentUnit(userId).getUnitValue());
        bet.setStake(calculateStake(bet));
        bet.setProfit(calculateProfit(bet));
        bet.setTotalValue(calculateTotalValue(bet));

        return bet;
    }

    private BigDecimal calculateProfit(Bet bet) {

        if (bet.getStatus() == Status.GREEN) {
            return bet.getStake()
                    .multiply(bet.getOdd().subtract(BigDecimal.ONE));
        }

        if (bet.getStatus() == Status.RED) {
            return bet.getStake().negate();
        }

        return BigDecimal.ZERO;
    }
    private BigDecimal calculateTotalValue(Bet bet) {

        if (bet.getStatus() == Status.GREEN) {
            return bet.getStake().multiply(bet.getOdd());
        }
        return BigDecimal.ZERO;
    }
    private BigDecimal calculateStake(Bet bet){
        return bet.getUnit().multiply(bet.getUnitValue());
    }
    private void processBetFinancials(Bet bet){
        // Sempre retira o valor da aposta do saldo
        transactionService.openBet(bet);

        // Se for cadastrada como green deposita o lucro
        if (bet.getStatus() == Status.GREEN) {
            transactionService.betWin(bet);
        }
    }

    public BetResponse update(Long userId, Long betId, BetRequest betRequest){
        Bet bet = getOwnedBet(userId, betId);
        //Valida se a banca do request é a mesma da Bet já salva, por padrão não será aceita essa alteração
        Banca banca = bancaService.getOwnedBanca(userId, betRequest.getBancaId());
        if (!banca.getId().equals(bet.getBanca().getId())) {
            throw new BusinessException("Não é permitido alterar a banca da bet");
        }

        Tipster tipster = tipsterService.getOwnedTipster(userId, betRequest.getTipsterId());

        //Salva valores atuais importantes da Bet
        BigDecimal oldOdd = bet.getOdd();
        Status oldStatus = bet.getStatus();
        BigDecimal oldUnit = bet.getUnit();

        bet.setTipster(tipster);
        bet.setSport(betRequest.getSport());
        bet.setOdd(betRequest.getOdd());
        bet.setStatus(betRequest.getStatus());
        bet.setHomeTeam(betRequest.getHomeTeam());
        bet.setAwayTeam(betRequest.getAwayTeam());
        bet.setDescription(betRequest.getDescription());
        bet.setMatchDate(betRequest.getMatchDate());
        bet.setUnit(betRequest.getUnit());

        if (!oldOdd.equals(bet.getOdd()) ||
                !oldStatus.equals(bet.getStatus()) ||
                !oldUnit.equals(bet.getUnit())) {
                    recalculateBetValues(bet);

        }

        handleBetStatusChange(oldStatus, bet);

        betRepository.save(bet);

        return betMapper.toResponse(bet);
    }
    private void recalculateBetValues(Bet bet){
        bet.setStake(calculateStake(bet));
        bet.setTotalValue(calculateTotalValue(bet));
        bet.setProfit(calculateProfit(bet));
    }

    private void handleBetStatusChange(Status oldStatus, Bet bet) {
        Status newStatus = bet.getStatus();
        Banca banca = bet.getBanca();
        //Altera pending/ red
        if (oldStatus.equals(Status.PENDING) || oldStatus.equals(Status.RED)) {
            switch (newStatus) {

                case GREEN -> transactionService.betWin(bet);

                case VOID -> transactionService.betVoid(bet);

                default -> {
                    // não faz nada, pois se for de pending -> red ou red -> pending o valor já foi debitado no cadastro da bet
                }
            }
        }
        //altera green
        if (oldStatus.equals(Status.GREEN)) {

            switch (newStatus) {

                case RED, PENDING -> {
                    transactionService.deleteTransactionFromBet(bet, oldStatus);
                }
                case VOID -> {
                    transactionService.deleteTransactionFromBet(bet, oldStatus);
                    transactionService.betVoid(bet);
                }
            }
        }
        //altera bet anulada
        if(oldStatus.equals(Status.VOID)){
            switch (newStatus) {
                case GREEN -> {
                    transactionService.deleteTransactionFromBet(bet, oldStatus);
                    transactionService.betWin(bet);
                }
                case RED, PENDING ->{
                    transactionService.deleteTransactionFromBet(bet, oldStatus);
                }
            }
        }
    }
    public BetResponse updateBetStatus(Long userId, Long betId, BetStatusRequest betStatusRequest){
        Bet bet = getOwnedBet(userId, betId);
        Status oldStatus = bet.getStatus();
        bet.setStatus(betStatusRequest.getStatus());
        handleBetStatusChange(oldStatus, bet);
        recalculateBetValues(bet);
        betRepository.save(bet);
        return betMapper.toResponse(bet);
    }


    public List<BetResponse> listAll(Long userId){
        List<Bet> bets = betRepository.findAllByUsuarioId(userId);
        return bets.stream()
                .map(bet -> betMapper.toResponse(bet))
                .toList();
    }

    public List<BetResponse> listAllPending(Long userId){
        List<Bet> bets = betRepository.findAllByUsuarioIdAndStatus(userId, Status.PENDING);
        if(bets.size()<1){
            throw new NotFoundException("Não existem apostas Pendentes");
        }
        return bets.stream()
                .map(bet -> betMapper.toResponse(bet))
                .toList();
    }

    private Bet getOwnedBet(Long userId, Long betId){
        return betRepository.findByIdAndUsuarioId(betId, userId)
                .orElseThrow(()-> new NotFoundException("Não existe bet cadastrada com esse Id."));
    }
}
