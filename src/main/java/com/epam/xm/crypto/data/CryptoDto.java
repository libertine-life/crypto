package com.epam.xm.crypto.data;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for the Crypto.
 */
@Data
@NoArgsConstructor
public class CryptoDto {

    @CsvBindByName(column = "timestamp")
    private Long timestamp;
    @CsvBindByName(column = "symbol")
    private String symbol;
    @CsvBindByName(column = "price")
    private BigDecimal price;
}
