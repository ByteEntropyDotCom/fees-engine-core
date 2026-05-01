package com.byteentropy.fees_engine_core.strategy;

import com.byteentropy.fees_engine_core.model.FeeRule;
import java.math.BigDecimal;

public sealed interface FeeStrategy permits FixedFeeStrategy, PercentageFeeStrategy {
    BigDecimal calculate(BigDecimal amount, FeeRule rule);
}