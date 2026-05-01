package com.byteentropy.fees_engine_core.strategy;

import com.byteentropy.fees_engine_core.model.FeeRule;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public final class FixedFeeStrategy implements FeeStrategy {
    @Override
    public BigDecimal calculate(BigDecimal amount, FeeRule rule) {
        return rule.value();
    }
}