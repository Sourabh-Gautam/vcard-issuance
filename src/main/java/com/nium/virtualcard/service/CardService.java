package com.nium.virtualcard.service;

import com.nium.virtualcard.dto.request.CreateCardRequest;
import com.nium.virtualcard.entity.Card;
import com.nium.virtualcard.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {
    Card createCard(CreateCardRequest request);
    Card spend(Long cardId, BigDecimal amount);
    Card topUp(Long cardId, BigDecimal amount);
    List<Transaction> getTransactions(Long cardId);
    Card getCard(Long cardId);
}
