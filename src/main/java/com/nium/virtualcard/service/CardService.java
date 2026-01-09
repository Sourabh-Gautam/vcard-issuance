package com.nium.virtualcard.service;

import com.nium.virtualcard.dto.request.CreateCardRequest;
import com.nium.virtualcard.entity.Card;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

public interface CardService {
    Card createCard(CreateCardRequest request);
    Card spend(Long cardId, BigDecimal amount);
    Card getCard(Long cardId);
}
