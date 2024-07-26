package com.epam.xm.crypto.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for the Crypto data with normalization.
 */
@Data
@NoArgsConstructor
public class CryptoNormalizedDto {
    private String symbol;
    private BigDecimal normalizedRange;
}
