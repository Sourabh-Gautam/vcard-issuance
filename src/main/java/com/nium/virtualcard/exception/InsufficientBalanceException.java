package com.nium.virtualcard.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(BigDecimal balance, BigDecimal amount) {
        super("Insufficient balance. Available: " + balance + ", Requested: " + amount);
    }
}
