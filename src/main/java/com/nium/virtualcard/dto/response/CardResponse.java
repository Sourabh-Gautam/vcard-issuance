package com.nium.virtualcard.dto.response;

import com.nium.virtualcard.entity.Card;
import com.nium.virtualcard.service.CardService;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class CardResponse {

    private final Long id;
    private final String cardholderName;
    private final BigDecimal balance;
    private final Instant createdAt;

    public CardResponse(Card card) {
        this.id = card.getId();
        this.cardholderName = card.getCardholderName();
        this.balance = card.getBalance();
        this.createdAt = card.getCreatedAt();
    }

}

