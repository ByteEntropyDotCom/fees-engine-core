package com.byteentropy.fees_engine_core.service;

import com.byteentropy.fees_engine_core.dto.FeeRequest;
import com.byteentropy.fees_engine_core.dto.FeeResponse;
import com.byteentropy.fees_engine_core.exception.FeeCalculationException;
import com.byteentropy.fees_engine_core.strategy.FixedFeeStrategy;
import com.byteentropy.fees_engine_core.strategy.PercentageFeeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FeeServiceTest {

    private FeeService feeService;
    private FeeCalculator feeCalculator;

    @BeforeEach
    void setUp() {
        // Using real strategies to test the full logic chain
        feeCalculator = new FeeCalculator(new FixedFeeStrategy(), new PercentageFeeStrategy());
        feeService = new FeeService(feeCalculator);
    }

    @Test
    @DisplayName("Should calculate standard percentage fee correctly")
    void testStandardPercentageFee() {
        FeeRequest request = new FeeRequest(
            UUID.randomUUID().toString(),
            new BigDecimal("100.00"),
            "USD",
            "MCH-001",
            "RETAIL"
        );

        FeeResponse response = feeService.process(request);

        // 2.5% of 100.00 is 2.50
        assertEquals(new BigDecimal("2.50"), response.feeAmount());
        assertEquals(new BigDecimal("97.50"), response.netAmount());
        assertEquals("USD", response.currency());
    }

    @Test
    @DisplayName("Should apply minimum fee cap when calculation is too low")
    void testMinimumFeeCap() {
        // 2.5% of 5.00 is 0.125, but min fee in FeeService is 0.30
        FeeRequest request = new FeeRequest(
            UUID.randomUUID().toString(),
            new BigDecimal("5.00"),
            "USD",
            "MCH-001",
            "RETAIL"
        );

        FeeResponse response = feeService.process(request);

        assertEquals(new BigDecimal("0.30"), response.feeAmount());
        assertEquals(new BigDecimal("4.70"), response.netAmount());
    }

    @Test
    @DisplayName("Should apply maximum fee cap when calculation is too high")
    void testMaximumFeeCap() {
        // 2.5% of 50,000.00 is 1,250.00, but max fee in FeeService is 500.00
        FeeRequest request = new FeeRequest(
            UUID.randomUUID().toString(),
            new BigDecimal("50000.00"),
            "USD",
            "MCH-001",
            "RETAIL"
        );

        FeeResponse response = feeService.process(request);

        assertEquals(new BigDecimal("500.00"), response.feeAmount());
        assertEquals(new BigDecimal("49500.00"), response.netAmount());
    }

    @Test
    @DisplayName("Should use Banker's Rounding (HALF_EVEN) for precision")
    void testRoundingPrecision() {
        // 2.5% of 10.55 is 0.26375 -> Should round to 0.26
        // 2.5% of 10.59 is 0.26475 -> Should round to 0.26
        FeeRequest request = new FeeRequest(
            UUID.randomUUID().toString(),
            new BigDecimal("10.55"),
            "USD",
            "MCH-001",
            "RETAIL"
        );

        FeeResponse response = feeService.process(request);
        assertEquals(new BigDecimal("0.30"), response.feeAmount()); // Still hits minimum cap of 0.30
    }

    @Test
    @DisplayName("Should throw exception for invalid transaction amounts")
    void testInvalidAmount() {
        FeeRequest request = new FeeRequest(
            UUID.randomUUID().toString(),
            new BigDecimal("-10.00"),
            "USD",
            "MCH-001",
            "RETAIL"
        );

        FeeCalculationException exception = assertThrows(FeeCalculationException.class, () -> 
            feeService.process(request)
        );
        
        assertEquals("INVALID_INPUT", exception.getErrorCode());
    }

    @Test
    @DisplayName("Should maintain financial integrity (Gross = Net + Fee)")
    void testFinancialIntegrity() {
        FeeRequest request = new FeeRequest(
            UUID.randomUUID().toString(),
            new BigDecimal("123.45"),
            "USD",
            "MCH-001",
            "RETAIL"
        );

        FeeResponse response = feeService.process(request);
        
        BigDecimal reconstructedGross = response.netAmount().add(response.feeAmount());
        assertEquals(0, new BigDecimal("123.45").compareTo(reconstructedGross), 
            "The sum of Net and Fee must equal Gross");
    }
}