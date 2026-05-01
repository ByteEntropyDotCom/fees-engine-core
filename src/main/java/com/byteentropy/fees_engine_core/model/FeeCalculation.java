package com.byteentropy.fees_engine_core.model;

import java.time.Instant;

/**
 * Represents the final result of a fee processing execution.
 * Designed as a record to ensure immutability across Virtual Threads.
 */
public record FeeCalculation(
    String transactionId,
    Money grossAmount,
    Money feeAmount,
    Money netAmount,
    String appliedRuleId,
    Instant calculatedAt
) {
    public FeeCalculation {
        if (transactionId == null || transactionId.isBlank()) {
            throw new IllegalArgumentException("Transaction ID is required for calculation results");
        }
        if (calculatedAt == null) {
            calculatedAt = Instant.now();
        }
    }

    /**
     * Helper to verify if the calculation preserves financial integrity.
     * gross = net + fee
     */
    public boolean isValid() {
        return grossAmount.amount()
            .compareTo(netAmount.amount().add(feeAmount.amount())) == 0;
    }
}
