package com.epam.xm.crypto.repository;

import com.epam.xm.crypto.data.CryptoStatisticsProjection;
import com.epam.xm.crypto.entity.CryptoStatisticsEntity;
import com.epam.xm.crypto.entity.CryptoStatisticsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link CryptoStatisticsEntity}.
 */
@Repository
public interface CryptoStatisticsRepository extends JpaRepository<CryptoStatisticsEntity, CryptoStatisticsKey> {

    /**
     * Find Crypto statistics entities based on a given symbol, month and year.
     *
     * @param symbol the symbol of the cryptocurrency
     * @param month the month
     * @param year the year
     * @return the list of {@link CryptoStatisticsEntity}
     */
    List<CryptoStatisticsEntity> findBySymbolAndMonthAndYear(String symbol, Integer month, Integer year);

    /**
     * Find a list of Crypto statistics projections based on a given symbol and time period.
     *
     * @param symbol the symbol of the cryptocurrency
     * @param startMonth the starting month of the time period
     * @param startYear the starting year of the time period
     * @param endMonth the ending month of the time period
     * @param endYear the ending year of the time period
     * @return the list of projection of type {@link CryptoStatisticsProjection}
     */
    @Query(value = "WITH per AS ( "
            + "  SELECT * FROM crypto_statistics cs "
            + "  WHERE  "
            + "    symbol = :symbol "
            + "    AND ( "
            + "      year = :startYear AND month >= :startMonth "
            + "      OR year > :startYear AND year < :endYear "
            + "      OR year = :endYear AND month <= :endMonth "
            + "    ) "
            + ") "
            + "SELECT  "
            + "  per.symbol, "
            + "  MIN(per.min_value) AS minValue, "
            + "  MAX(per.max_value) AS maxValue, "
            + "  (SELECT per1.oldest_value FROM per per1 ORDER BY year, month LIMIT 1) AS oldestValue, "
            + "  (SELECT per2.newest_value FROM per per2 ORDER BY year DESC, month DESC LIMIT 1) AS newestValue "
            + "FROM per    "
            + "GROUP BY  "
            + "  symbol ",
            nativeQuery = true)
    List<CryptoStatisticsProjection> findBySymbolAndPeriod(@Param("symbol") String symbol,
                                                           @Param("startMonth") Integer startMonth,
                                                           @Param("startYear") Integer startYear,
                                                           @Param("endMonth") Integer endMonth,
                                                           @Param("endYear") Integer endYear);
}
