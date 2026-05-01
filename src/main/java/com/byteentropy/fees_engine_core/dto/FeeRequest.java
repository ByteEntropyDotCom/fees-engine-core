package com.byteentropy.fees_engine_core.dto;

import java.math.BigDecimal;

public record FeeRequest(
    String transactionId,
    BigDecimal amount,
    String currency,
    String merchantId,
    String transactionCategory
) {}
