package com.luigiceschim.board_project.repotories;

import com.luigiceschim.board_project.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
