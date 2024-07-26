package com.epam.xm.crypto.service;

import com.epam.xm.crypto.data.CryptoNormalizedDto;
import com.epam.xm.crypto.data.CryptoStatisticsDto;
import com.epam.xm.crypto.entity.CryptoStatisticsEntity;
import com.epam.xm.crypto.entity.CryptoStatisticsKey;
import com.epam.xm.crypto.mapper.CryptoMapper;
import com.epam.xm.crypto.repository.CryptoRepository;
import com.epam.xm.crypto.repository.CryptoStatisticsRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Service handling operations related to CryptoStatistics.
 */
@Service
@AllArgsConstructor
public class CryptoStatisticsService {
    private final CryptoStatisticsRepository cryptoStatisticsRepository;
    private final CryptoRepository cryptoRepository;

    private final CryptoMapper cryptoMapper;

    /**
     * Recalculate statistics for the given set of CryptoStatisticsKey ranges.
     *
     * @param calculationRanges the CryptoStatisticsKey ranges.
     */
    public void recalculateStatistics(Set<CryptoStatisticsKey> calculationRanges) {
        if (CollectionUtils.isEmpty(calculationRanges)) {
            return;
        }
        calculationRanges.forEach(range -> {
            recalculate(range);
        });
    }

    /**
     * Recalculate statistics for the given CryptoStatisticsKey range.
     *
     * @param range the CryptoStatisticsKey range.
     */
    private void recalculate(CryptoStatisticsKey range) {
        if (range == null) {
            return;
        }

        cryptoRepository.findStatisticsByKey(range.getSymbol(), range.getMonth(), range.getYear())
                .ifPresent(proj -> {
                    CryptoStatisticsEntity en = new CryptoStatisticsEntity();
                    en.setMonth(range.getMonth());
                    en.setYear(range.getYear());
                    en.setSymbol(range.getSymbol());
                    en.setMinValue(proj.getMinValue());
                    en.setMaxValue(proj.getMaxValue());
                    en.setOldestValue(proj.getOldestValue());
                    en.setNewestValue(proj.getNewestValue());
                    cryptoStatisticsRepository.save(en);
                });
    }

    /**
     * Provides a list of CryptoStatisticsDto using the given parameters.
     *
     * @param symbol     representing the crypto symbol.
     * @param startMonth the month to start the extraction.
     * @param startYear  the year to start the extraction.
     * @param endMonth   the month to end the extraction.
     * @param endYear    the year to end the extraction.
     * @return a list of CryptoStatisticsDto.
     */
    public List<CryptoStatisticsDto> getStatistics(String symbol, Integer startMonth, Integer startYear, Integer endMonth, Integer endYear) {
        if (symbol == null || startMonth == null || startYear == null || endMonth == null || endYear == null) {
            throw new IllegalArgumentException("Invalid empty parameters");
        }
        if (startMonth.equals(endMonth) && startYear.equals(endYear)) {
            return cryptoMapper.toCryptoStatisticsDtosFromEntities(
                    cryptoStatisticsRepository.findBySymbolAndMonthAndYear(symbol, startMonth, startYear));
        } else {
            return cryptoMapper.toCryptoStatisticsDtosFromProjections(
                    cryptoStatisticsRepository.findBySymbolAndPeriod(symbol, startMonth, startYear, endMonth, endYear));
        }
    }

    /**
     * Find a list of Crypto normalized range statistics sorted ascending.
     *
     * @return the list of dto CryptoNormalizedDto
     */
    public List<CryptoNormalizedDto> getCryptoNormalized() {
        return cryptoMapper.toCryptoNormalizedDtos(
                cryptoRepository.findCryptoStatisticsOrderByNormalized());
    }
}
