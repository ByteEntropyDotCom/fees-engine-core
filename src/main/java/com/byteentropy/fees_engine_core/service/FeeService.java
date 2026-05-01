package com.byteentropy.fees_engine_core.service;

import com.byteentropy.fees_engine_core.exception.FeeCalculationException;
import com.byteentropy.fees_engine_core.model.*;
import com.byteentropy.fees_engine_core.dto.FeeRequest;
import com.byteentropy.fees_engine_core.dto.FeeResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.math.BigDecimal;

@Service
public class FeeService {

    private final FeeCalculator feeCalculator;

    public FeeService(FeeCalculator feeCalculator) {
        this.feeCalculator = feeCalculator;
    }

    public FeeResponse process(FeeRequest request) {
        // Defensive check for zero/negative amounts
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new FeeCalculationException("Transaction amount must be positive", "INVALID_INPUT");
        }

        // Fetching rule (In 2026, this would come from an @Cacheable repository)
        FeeRule rule = new FeeRule(
            "RULE-STD-001",
            FeeType.PERCENTAGE,
            new BigDecimal("0.025"), // 2.5%
            new BigDecimal("0.30"),  // $0.30 Min
            new BigDecimal("500.00") // $500 Max
        );

        Money gross = new Money(request.amount(), request.currency());
        
        try {
            Money fee = feeCalculator.calculateFee(gross, rule);
            
            FeeCalculation result = new FeeCalculation(
                request.transactionId(),
                gross,
                fee,
                new Money(gross.amount().subtract(fee.amount()), gross.currency()),
                rule.ruleId(),
                Instant.now()
            );

            if (!result.isValid()) {
                throw new FeeCalculationException("Calculated amounts do not balance", "INTEGRITY_ERROR");
            }

            return new FeeResponse(
                result.transactionId(),
                result.feeAmount().amount(),
                result.netAmount().amount(),
                result.grossAmount().currency()
            );

        } catch (Exception e) {
            throw new FeeCalculationException(e.getMessage(), "CALCULATION_FAILURE");
        }
    }
}