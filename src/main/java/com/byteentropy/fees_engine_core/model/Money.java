package com.byteentropy.fees_engine_core.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount == null) throw new IllegalArgumentException("Amount cannot be null");
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Money of(String amount, String currency) {
        return new Money(new BigDecimal(amount), currency);
    }
}