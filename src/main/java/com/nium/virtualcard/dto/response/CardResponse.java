package com.nium.virtualcard.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
public class CardResponse {

    private Long id;
    private String cardholderName;
    private BigDecimal balance;
    private Instant createdAt;

}
