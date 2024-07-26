package com.epam.xm.crypto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representation of a Crypto currency record stored in database.
 */
@Entity
@Table(name = "crypto_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "price_date", nullable = false)
    private LocalDateTime priceDate;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "year", nullable = false)
    private Integer year;
}