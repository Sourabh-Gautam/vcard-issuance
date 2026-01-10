package com.nium.virtualcard.dto.response;

import com.nium.virtualcard.constant.TransactionType;
import com.nium.virtualcard.entity.Transaction;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class TransactionResponse {

    private final Long id;
    private final BigDecimal amount;
    private final TransactionType type;
    private final Instant createdAt;

    public TransactionResponse(Transaction tx) {
        this.id = tx.getId();
        this.amount = tx.getAmount();
        this.type = tx.getType();
        this.createdAt = tx.getCreatedAt();
    }

}
