package com.byteentropy.fees_engine_core.model;

import java.math.BigDecimal;

public record FeeRule(
    String ruleId,
    FeeType type,
    BigDecimal value, // Either fixed amount or percentage (e.g., 0.029 for 2.9%)
    BigDecimal minLimit,
    BigDecimal maxLimit
) {}