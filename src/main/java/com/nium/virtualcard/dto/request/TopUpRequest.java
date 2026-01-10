package com.nium.virtualcard.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TopUpRequest {

    @NotNull
    @Positive(message = "Top-up amount must be positive")
    private BigDecimal amount;

}
