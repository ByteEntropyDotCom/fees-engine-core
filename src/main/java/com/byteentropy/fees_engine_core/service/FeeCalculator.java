package com.byteentropy.fees_engine_core.service;

import com.byteentropy.fees_engine_core.model.*;
import com.byteentropy.fees_engine_core.strategy.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class FeeCalculator {

    private final Map<FeeType, FeeStrategy> strategyRegistry;

    public FeeCalculator(FixedFeeStrategy fixed, PercentageFeeStrategy percentage) {
        this.strategyRegistry = Map.of(
            FeeType.FIXED, fixed,
            FeeType.PERCENTAGE, percentage
        );
    }

    public Money calculateFee(Money transactionAmount, FeeRule rule) {
        FeeStrategy strategy = strategyRegistry.get(rule.type());
        
        if (strategy == null) {
            throw new UnsupportedOperationException("Fee type not implemented: " + rule.type());
        }

        // Pattern matching for strategy execution (2026 Style)
        BigDecimal feeAmount = switch (strategy) {
            case FixedFeeStrategy s -> s.calculate(transactionAmount.amount(), rule);
            case PercentageFeeStrategy s -> s.calculate(transactionAmount.amount(), rule);
        };

        // Apply caps with precision
        if (rule.maxLimit() != null) feeAmount = feeAmount.min(rule.maxLimit());
        if (rule.minLimit() != null) feeAmount = feeAmount.max(rule.minLimit());

        return new Money(feeAmount.setScale(2, RoundingMode.HALF_EVEN), transactionAmount.currency());
    }
}
