package com.nium.virtualcard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RemainingBalanceResponse {
    private final Long cardId;
    private final BigDecimal remainingBalance;
}
