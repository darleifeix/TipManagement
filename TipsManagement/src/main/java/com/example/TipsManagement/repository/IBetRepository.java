package com.example.TipsManagement.repository;

import com.example.TipsManagement.model.Bet;
import com.example.TipsManagement.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBetRepository extends JpaRepository<Bet, Long> {
    Optional<Bet> findByIdAndUsuarioId(Long betId, Long userId);
    List<Bet> findAllByUsuarioId(Long userId);
    List<Bet> findAllByUsuarioIdAndStatus(Long userId, Status status);

}
