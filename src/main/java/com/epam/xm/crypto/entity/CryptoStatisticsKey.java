package com.epam.xm.crypto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Key class for CryptoStatisticsEntity.
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CryptoStatisticsKey implements Serializable {
    private String symbol;
    private int month;
    private int year;
}