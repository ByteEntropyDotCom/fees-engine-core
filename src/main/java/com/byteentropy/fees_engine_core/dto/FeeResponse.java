package com.byteentropy.fees_engine_core.dto;

import java.math.BigDecimal;

public record FeeResponse(
    String transactionId,
    BigDecimal feeAmount,
    BigDecimal netAmount,
    String currency
) {}
