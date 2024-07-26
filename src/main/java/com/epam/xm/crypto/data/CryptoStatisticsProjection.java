package com.epam.xm.crypto.data;

import java.math.BigDecimal;

/**
 * Projection interface for CryptoStatistics data.
 */
public interface CryptoStatisticsProjection {
    String getSymbol();
    BigDecimal getMinValue();
    BigDecimal getMaxValue();
    BigDecimal getOldestValue();
    BigDecimal getNewestValue();
}
