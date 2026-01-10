package com.nium.virtualcard.service.impl;

import com.nium.virtualcard.constant.TransactionType;
import com.nium.virtualcard.dto.request.CreateCardRequest;
import com.nium.virtualcard.entity.Card;
import com.nium.virtualcard.entity.Transaction;
import com.nium.virtualcard.exception.CardNotFoundException;
import com.nium.virtualcard.exception.InsufficientBalanceException;
import com.nium.virtualcard.repository.CardRepository;
import com.nium.virtualcard.repository.TransactionRepository;
import com.nium.virtualcard.service.CardService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    public CardServiceImpl(CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    @Override
    public Card createCard(CreateCardRequest request) {
        Card card = new Card();
        card.setCardholderName(request.getCardholderName());
        card.setBalance(BigDecimal.valueOf(0));

        Card savedCard = cardRepository.save(card);
        topUp(savedCard.getId(), request.getInitialBalance()); // It will create a transaction for initial balance as well

        return savedCard;
    }

    @Transactional
    @Override
    public Card spend(Long cardId, BigDecimal amount) {
        Card card = getCard(cardId);

        if (card.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(card.getBalance(), amount);
        }

        card.setBalance(card.getBalance().subtract(amount));

        Transaction tx = new Transaction();
        tx.setCard(card);
        tx.setAmount(amount);
        tx.setType(TransactionType.SPEND);

        transactionRepository.save(tx);
        return cardRepository.save(card);
    }

    @Override
    public Card topUp(Long cardId, BigDecimal amount) {
        Card card = getCard(cardId);

        card.setBalance(card.getBalance().add(amount));

        Transaction tx = new Transaction();
        tx.setCard(card);
        tx.setAmount(amount);
        tx.setType(TransactionType.TOP_UP);

        transactionRepository.save(tx);

        return cardRepository.save(card);
    }

    @Override
    public List<Transaction> getTransactions(Long cardId) {
        getCard(cardId); // validates card existence
        return transactionRepository.findByCardIdOrderByCreatedAtDesc(cardId);
    }

    @Override
    public Card getCard(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
    }
}
