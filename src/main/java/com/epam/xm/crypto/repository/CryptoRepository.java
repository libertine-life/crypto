package com.epam.xm.crypto.repository;

import com.epam.xm.crypto.data.CryptoNormalizedProjection;
import com.epam.xm.crypto.data.CryptoStatisticsProjection;
import com.epam.xm.crypto.entity.CryptoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link CryptoEntity}.
 */
@Repository
public interface CryptoRepository extends JpaRepository<CryptoEntity, String> {

    /**
     * Find Crypto statistics based on given symbol, month and year.
     *
     * @param symbol the symbol of the crypto
     * @param month the month
     * @param year the year
     * @return the projection of type {@link CryptoStatisticsProjection}
     */
    @Query(value = "SELECT "
            + "      symbol,"
            + "      year,"
            + "      month,"
            + "      MIN(price) AS minValue,"
            + "      MAX(price) AS maxValue,"
            + "      (SELECT price FROM crypto_record cr1 WHERE cr1.symbol = cr.symbol AND cr1.month = cr.month AND cr1.year = cr.year ORDER BY price_date LIMIT 1) AS oldestValue,"
            + "      (SELECT price FROM crypto_record cr2 WHERE cr2.symbol = cr.symbol AND cr2.month = cr.month AND cr2.year = cr.year ORDER BY price_date DESC LIMIT 1) AS newestValue"
            + "   FROM "
            + "      crypto_record AS cr"
            + "   WHERE "
            + "      symbol = :symbol"
            + "      AND month = :month"
            + "      AND year = :year"
            + "   GROUP BY "
            + "     symbol, year, month", nativeQuery = true)
    Optional<CryptoStatisticsProjection> findStatisticsByKey(@Param("symbol") String symbol,
                                                   @Param("month") Integer month,
                                                   @Param("year") Integer year);

    /**
     * Find a list of Crypto normalized range statistics sorted.
     *
     * @return the list of projection of type {@link CryptoNormalizedProjection}
     */
    @Query(value = " WITH stat as ( "
            + " SELECT cs.symbol, "
            + "   min(cs.min_value) as min_value, "
            + "   max(cs.max_value) as max_value  "
            + " FROM crypto_statistics cs "
            + " GROUP BY cs.symbol) "
            + " SELECT  "
            + "   symbol,  "
            + "   (max_value-min_value)/min_value as normalizedRange "
            + " FROM stat "
            + " ORDER BY normalizedRange ",
            nativeQuery = true)
    List<CryptoNormalizedProjection> findCryptoStatisticsOrderByNormalized();

    /**
     * Find Cryptocurrency with highest normalized range for given date.
     *
     * @param date the date that the normalized range is based on
     * @return the projection of type {@link CryptoNormalizedProjection}
     */
    @Query(value = " WITH rec as ( "
            + "SELECT cr.symbol,  "
            + "   min(cr.price) as min_value, "
            + "   max(cr.price) as max_value  "
            + "FROM crypto_record cr  "
            + "WHERE DATE(price_date) = :date "
            + "GROUP BY cr.symbol "
            + ")  "
            + "SELECT "
            + "  rec.symbol, "
            + "  ((max_value - min_value) / min_value) AS normalizedRange "
            + "FROM rec "
            + "ORDER BY normalizedRange DESC "
            + "LIMIT 1; ",
            nativeQuery = true)
    Optional<CryptoNormalizedProjection> findHighestNormalizedRange(@Param("date") LocalDate date);

}
