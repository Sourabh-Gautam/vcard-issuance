package com.nium.virtualcard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateCardRequest {
    @NotBlank(message = "Cardholder name must not be empty")
    private String cardholderName;

    @Positive(message = "Initial balance must be greater than zero")
    private BigDecimal initialBalance;
}