package com.nium.virtualcard.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class SpendRequest {

    @Positive
    private BigDecimal amount;

}
