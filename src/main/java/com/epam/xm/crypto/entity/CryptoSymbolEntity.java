package com.epam.xm.crypto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Entity representation of a Crypto currency symbol stored in database.
 */
@Entity
@Table(name = "crypto_symbol")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoSymbolEntity implements Serializable {

    @Id
    @Column(name = "symbol")
    private String symbol;
}