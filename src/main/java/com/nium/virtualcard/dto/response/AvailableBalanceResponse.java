package com.nium.virtualcard.dto.response;

import java.math.BigDecimal;

public record AvailableBalanceResponse(Long cardId, BigDecimal availableBalance) {
}
