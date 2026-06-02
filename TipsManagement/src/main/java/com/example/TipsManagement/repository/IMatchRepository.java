package com.example.TipsManagement.repository;

import com.example.TipsManagement.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface IMatchRepository extends JpaRepository<Match, Long> {
    void deleteByMatchDateBefore(OffsetDateTime date);

    List<Match> findByMatchDateBefore(OffsetDateTime date);
}
