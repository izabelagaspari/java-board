package com.luigiceschim.board_project.services.colunaService;

import com.luigiceschim.board_project.dto.card.CardRequestDTO;
import com.luigiceschim.board_project.dto.card.CardResponseDTO;
import com.luigiceschim.board_project.entities.Card;
import com.luigiceschim.board_project.entities.Coluna;

public interface ColunaService {
    CardResponseDTO createCard(CardRequestDTO requestDTO, Long id);
    Coluna removeCard(Long id);
    void moveCard(Long id,Long boardId);


}
