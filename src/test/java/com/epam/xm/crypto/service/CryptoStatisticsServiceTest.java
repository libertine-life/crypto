package com.epam.xm.crypto.service;

import com.epam.xm.crypto.data.CryptoNormalizedDto;
import com.epam.xm.crypto.data.CryptoStatisticsDto;
import com.epam.xm.crypto.data.CryptoStatisticsProjection;
import com.epam.xm.crypto.entity.CryptoStatisticsEntity;
import com.epam.xm.crypto.mapper.CryptoMapper;
import com.epam.xm.crypto.repository.CryptoRepository;
import com.epam.xm.crypto.repository.CryptoStatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class CryptoStatisticsServiceTest {

    @Mock
    private CryptoStatisticsRepository cryptoStatisticsRepository;

    @Mock
    private CryptoRepository cryptoRepository;

    @Mock
    private CryptoMapper cryptoMapper;

    @InjectMocks
    private CryptoStatisticsService cryptoStatisticsService;

    @Test
    void testGetStatistics() {

        when(cryptoStatisticsRepository.findBySymbolAndMonthAndYear(anyString(), anyInt(), anyInt()))
                .thenReturn(Arrays.asList(new CryptoStatisticsEntity()));
        when(cryptoMapper.toCryptoStatisticsDtosFromEntities(anyList()))
                .thenReturn(Arrays.asList(new CryptoStatisticsDto()));

        List<CryptoStatisticsDto> result = cryptoStatisticsService.getStatistics("BTC", 2, 2020, 2, 2020);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetCryptoNormalized() {

        when(cryptoRepository.findCryptoStatisticsOrderByNormalized())
                .thenReturn(Mockito.mock(List.class));
        when(cryptoMapper.toCryptoNormalizedDtos(anyList()))
                .thenReturn(Arrays.asList(new CryptoNormalizedDto()));

        List<CryptoNormalizedDto> result = cryptoStatisticsService.getCryptoNormalized();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetStatistics_withNullSymbol() {
        assertThrows(IllegalArgumentException.class,
                () -> cryptoStatisticsService.getStatistics(null, 2, 2020, 2, 2020));
    }

    @Test
    void testGetStatistics_withNullStartMonth() {
        assertThrows(IllegalArgumentException.class,
                () -> cryptoStatisticsService.getStatistics("BTC", null, 2020, 2, 2020));
    }

    @Test
    void testGetStatistics_withNullStartYear() {
        assertThrows(IllegalArgumentException.class,
                () -> cryptoStatisticsService.getStatistics("BTC", 2, null, 2, 2020));
    }

    @Test
    void testGetStatistics_withNullEndMonth() {
        assertThrows(IllegalArgumentException.class,
                () -> cryptoStatisticsService.getStatistics("BTC", 2, 2020, null, 2020));
    }

    @Test
    void testGetStatistics_withNullEndYear() {
        assertThrows(IllegalArgumentException.class,
                () -> cryptoStatisticsService.getStatistics("BTC", 2, 2020, 2, null));

    }

    @Test
    void testGetStatistics_sameStartEndMonthYear() {
        when(cryptoStatisticsRepository.findBySymbolAndMonthAndYear(any(), anyInt(), anyInt()))
                .thenReturn(Arrays.asList(new CryptoStatisticsEntity()));
        when(cryptoMapper.toCryptoStatisticsDtosFromEntities(anyList()))
                .thenReturn(Arrays.asList(new CryptoStatisticsDto()));

        cryptoStatisticsService.getStatistics("BTC", 2, 2020, 2, 2020);

        verify(cryptoStatisticsRepository, times(1)).findBySymbolAndMonthAndYear(any(), anyInt(), anyInt());
        verify(cryptoStatisticsRepository, never()).findBySymbolAndPeriod(anyString(), anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    void testGetStatistics_differentStartEndMonthYear() {

        when(cryptoStatisticsRepository.findBySymbolAndPeriod(any(), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenReturn(Arrays.asList(Mockito.mock(CryptoStatisticsProjection.class)));
        when(cryptoMapper.toCryptoStatisticsDtosFromProjections(anyList()))
                .thenReturn(Arrays.asList(new CryptoStatisticsDto()));

        cryptoStatisticsService.getStatistics("BTC", 2, 2020, 3, 2020);

        verify(cryptoStatisticsRepository, never()).findBySymbolAndMonthAndYear(any(), anyInt(), anyInt());
        verify(cryptoStatisticsRepository, times(1)).findBySymbolAndPeriod(anyString(), anyInt(), anyInt(), anyInt(), anyInt());
    }
}