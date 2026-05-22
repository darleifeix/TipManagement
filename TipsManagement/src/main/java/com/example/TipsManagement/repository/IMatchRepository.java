package com.example.TipsManagement.repository;

import com.example.TipsManagement.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMatchRepository extends JpaRepository<Match, Long> {
}
