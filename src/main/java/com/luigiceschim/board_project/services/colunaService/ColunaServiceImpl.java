package com.luigiceschim.board_project.services.colunaService;

import com.luigiceschim.board_project.dto.card.CardRequestDTO;
import com.luigiceschim.board_project.dto.card.CardResponseDTO;
import com.luigiceschim.board_project.entities.Card;
import com.luigiceschim.board_project.entities.Coluna;
import com.luigiceschim.board_project.repotories.BoardRepository;
import com.luigiceschim.board_project.repotories.CardRepository;
import com.luigiceschim.board_project.repotories.ColunaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColunaServiceImpl implements ColunaService {

    private final BoardRepository repository;
    private final CardRepository cardRepository;
    private final ColunaRepository colunaRepository;

    public ColunaServiceImpl(BoardRepository repository, CardRepository cardRepository, ColunaRepository colunaRepository) {
        this.repository = repository;
        this.cardRepository = cardRepository;
        this.colunaRepository = colunaRepository;
    }


    @Override
    @Transactional
    public CardResponseDTO createCard(CardRequestDTO requestDTO, Long id) {
        var result = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        var colunaInicial = result.getColunas().stream().filter(c -> "Inicial".equals(c.getNome())).findFirst().orElseThrow(() -> new RuntimeException("deu merda"));

        Card card1 = new Card(requestDTO.titulo(), requestDTO.descricao());
        card1.setColuna(colunaInicial);
        colunaInicial.addCard(card1);

        cardRepository.save(card1);

        repository.save(result);
        return new CardResponseDTO(card1.getId(), requestDTO.titulo(), requestDTO.descricao());

    }

    @Override
    public Coluna removeCard(Long id) {
        var colunaResult = colunaRepository.findAll();

        for (var coluna : colunaResult) {
            // Filtra os cards da coluna pelo ID do card
            var cardResult = coluna.getCards().stream()
                    .filter(card -> card.getId().equals(id))
                    .findFirst();

            if (cardResult.isPresent()) {
                // Verifica se o tipo da coluna é FINAL ou CANCELADO

                var card = cardResult.get();

                // Remove o card da coluna
                coluna.removeCard(card);

                // Salva a coluna atualizada
                return colunaRepository.save(coluna);

            }
        }

        return null; // Retornar null caso o card não seja encontrado ou não seja removido
    }

    @Override
    @Transactional
    public void moveCard(Long id,Long boardId) {
        var colunaResult = repository.findById(boardId).orElseThrow(() -> new RuntimeException("deu merda"));

        var colunaAtual = colunaResult.getColunas().stream().filter(c -> c.getCards().stream().anyMatch(card -> card.getId().equals(id))).findFirst().orElseThrow(()-> new RuntimeException("deu bosta"));

        var card = colunaAtual.getCards().stream().filter(card1 -> card1.getId().equals(id)).findFirst().orElseThrow(()-> new RuntimeException("deu coco"));

        colunaAtual.getCards().remove(card);
        colunaRepository.save(colunaAtual);

        Integer proximaOrdem = colunaAtual.getOrdem() + 1;

        var proximaColuna = colunaResult.getColunas().stream().filter(coluna -> coluna.getOrdem().equals(proximaOrdem)).findFirst().orElseThrow(()-> new RuntimeException("deu lixo"));

         proximaColuna.getCards().add(card);
         card.setColuna(proximaColuna);
         cardRepository.save(card);


        colunaRepository.save(proximaColuna);

        repository.save(colunaResult);


    }
}