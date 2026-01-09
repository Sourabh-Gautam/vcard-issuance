package com.nium.virtualcard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateCardRequest {

    @NotBlank
    private String cardholderName;

    @Positive
    private BigDecimal initialBalance;

}