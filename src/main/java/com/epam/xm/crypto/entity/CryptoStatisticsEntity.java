package com.epam.xm.crypto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entity representation of Crypto currency statistics stored in the database.
 */
@Entity
@Table(name = "crypto_statistics")
@IdClass(CryptoStatisticsKey.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoStatisticsEntity implements Serializable {

    @Id
    @Column(name = "month", nullable = false)
    private Integer month;

    @Id
    @Column(name = "year", nullable = false)
    private Integer year;

    @Id
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "oldest_value", nullable = false)
    private BigDecimal oldestValue;

    @Column(name = "newest_value", nullable = false)
    private BigDecimal newestValue;

    @Column(name = "min_value", nullable = false)
    private BigDecimal minValue;

    @Column(name = "max_value", nullable = false)
    private BigDecimal maxValue;
}
