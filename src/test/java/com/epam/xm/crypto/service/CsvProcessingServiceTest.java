package com.epam.xm.crypto.service;

import com.epam.xm.crypto.entity.CryptoEntity;
import com.epam.xm.crypto.mapper.CryptoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvProcessingServiceTest {

    @Mock
    private CryptoService cryptoService;

    @Mock
    private CryptoStatisticsService cryptoStatisticsService;

    @Mock
    private CryptoSymbolService cryptoSymbolService;

    @Mock
    private CryptoMapper cryptoMapper;

    @InjectMocks
    private CsvProcessingService csvProcessingService;

    @Test
    void testReadCsv_processSuccessfully() throws Exception {
        String csvContent = "symbol,price,datetime\n"
                + "BTC,12345,74295872455342\n"; // fake CSV content with valid symbol "BTC"
        MultipartFile multipartFile = new MockMultipartFile(
                "data",
                "test.csv",
                "text/csv",
                csvContent.getBytes());

        CryptoEntity entity = new CryptoEntity();
        entity.setPrice(new BigDecimal(100));
        entity.setPriceDate(LocalDateTime.now());
        entity.setMonth(1);
        entity.setYear(2000);
        entity.setSymbol("BTC");

        when(cryptoMapper.toEntity(any())).thenReturn(entity);
        doNothing().when(cryptoService).saveBatch(any());
        doNothing().when(cryptoStatisticsService).recalculateStatistics(any());
        when(cryptoSymbolService.getAllowedSymbols()).thenReturn(Set.of("BTC"));

        csvProcessingService.readCsv(multipartFile);

        verify(cryptoService, times(1)).saveBatch(any());
    }

    @Test
    void testReadCsv_unknownSymbol() throws IOException {
        String csvContent = "symbol,price,datetime\n"
                + "UNKNOWN,12345,12741938462387462\n"; // fake CSV content with "UNKNOWN" symbol
        MultipartFile multipartFile = new MockMultipartFile(
                "data",
                "unknown_symbol.csv",
                "text/csv",
                csvContent.getBytes());

        when(cryptoSymbolService.getAllowedSymbols()).thenReturn(Set.of("BTC"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> csvProcessingService.readCsv(multipartFile));
        assertTrue(exception.getMessage().contains("Unknown crypto symbol UNKNOWN in the batch"));
    }
}