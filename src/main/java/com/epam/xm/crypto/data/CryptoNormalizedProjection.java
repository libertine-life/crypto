package com.epam.xm.crypto.data;

import java.math.BigDecimal;

/**
 * Projection interface for CryptoNormalized data.
 */
public interface CryptoNormalizedProjection {
    String getSymbol();
    BigDecimal getNormalizedRange();
}
