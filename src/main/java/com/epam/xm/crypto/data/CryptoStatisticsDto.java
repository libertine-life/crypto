package com.epam.xm.crypto.data;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for the statistical data of the Crypto.
 */
@NoArgsConstructor
@Data
public class CryptoStatisticsDto {
    private String symbol;
    private BigDecimal oldestValue;
    private BigDecimal newestValue;
    private BigDecimal minValue;
    private BigDecimal maxValue;
}
