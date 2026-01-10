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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    public CardServiceImpl(CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    @Override
    public Card createCard(CreateCardRequest request) {
        log.info("Creating card for cardholder: {}", request.getCardholderName());

        Card card = new Card();
        card.setCardholderName(request.getCardholderName());
        card.setBalance(BigDecimal.ZERO);

        Card savedCard = cardRepository.save(card);

        log.info("Card created successfully with id: {}", savedCard.getId());

        // Initial balance top-up (recorded as transaction)
        topUp(savedCard.getId(), request.getInitialBalance());

        return savedCard;
    }

    @Transactional
    @Override
    public Card spend(Long cardId, BigDecimal amount) {
        log.info("Spending amount {} from card {}", amount, cardId);

        Card card = getCard(cardId);

        if (card.getBalance().compareTo(amount) < 0) {
            log.warn("Insufficient balance for card {}. Available: {}, Requested: {}",
                    cardId, card.getBalance(), amount);
            throw new InsufficientBalanceException(card.getBalance(), amount);
        }

        card.setBalance(card.getBalance().subtract(amount));

        Transaction tx = new Transaction();
        tx.setCard(card);
        tx.setAmount(amount);
        tx.setType(TransactionType.SPEND);

        transactionRepository.save(tx);
        log.debug("SPEND transaction recorded for card {}", cardId);

        Card updatedCard = cardRepository.save(card);

        log.info("Spend successful for card {}. Remaining balance: {}",
                cardId, updatedCard.getBalance());

        return updatedCard;
    }

    @Override
    public Card topUp(Long cardId, BigDecimal amount) {
        log.info("Topping up amount {} to card {}", amount, cardId);

        Card card = getCard(cardId);

        card.setBalance(card.getBalance().add(amount));

        Transaction tx = new Transaction();
        tx.setCard(card);
        tx.setAmount(amount);
        tx.setType(TransactionType.TOP_UP);

        transactionRepository.save(tx);
        log.debug("TOP_UP transaction recorded for card {}", cardId);

        Card updatedCard = cardRepository.save(card);

        log.info("Top-up successful for card {}. New balance: {}",
                cardId, updatedCard.getBalance());

        return updatedCard;
    }

    @Override
    public List<Transaction> getTransactions(Long cardId) {
        log.debug("Fetching transactions for card {}", cardId);
        getCard(cardId); // validates card existence
        return transactionRepository.findByCardIdOrderByCreatedAtDesc(cardId);
    }

    @Override
    public Card getCard(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> {
                    log.warn("Card not found with id {}", cardId);
                    return new CardNotFoundException(cardId);
                });
    }
}
