package com.nium.virtualcard.service.impl;

import com.nium.virtualcard.dto.request.CreateCardRequest;
import com.nium.virtualcard.entity.Card;
import com.nium.virtualcard.exception.CardNotFoundException;
import com.nium.virtualcard.exception.InsufficientBalanceException;
import com.nium.virtualcard.repository.CardRepository;
import com.nium.virtualcard.service.CardService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    @Override
    public Card createCard(CreateCardRequest request) {
        Card card = new Card();
        card.setCardholderName(request.getCardholderName());
        card.setBalance(request.getInitialBalance());

        return cardRepository.save(card);
    }

    @Transactional
    @Override
    public Card spend(Long cardId, BigDecimal amount) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        if (card.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(card.getBalance(), amount);
        }

        card.setBalance(card.getBalance().subtract(amount));
        cardRepository.save(card);

        return card;
    }

    @Override
    public Card getCard(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
    }
}
