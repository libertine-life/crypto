package com.epam.xm.crypto.service;

import com.epam.xm.crypto.data.CryptoNormalizedDto;
import com.epam.xm.crypto.entity.CryptoEntity;
import com.epam.xm.crypto.mapper.CryptoMapper;
import com.epam.xm.crypto.repository.CryptoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptoServiceTest {

    @Mock
    private CryptoRepository cryptoRepository;

    @Mock
    private CryptoMapper cryptoMapper;

    @InjectMocks
    private CryptoService cryptoService;

    @Test
    void testSaveBatch_withNullCollection() {
        cryptoService.saveBatch(null);

        verify(cryptoRepository, times(0)).saveAll(any());
    }

    @Test
    void testSaveBatch_withEmptyCollection() {
        cryptoService.saveBatch(Collections.emptyList());

        verify(cryptoRepository, never()).saveAll(any());
    }

    @Test
    void testSaveBatch_withNonEmptyCollection() {
        Collection<CryptoEntity> entities = new ArrayList<>();
        entities.add(new CryptoEntity());
        cryptoService.saveBatch(entities);

        verify(cryptoRepository, times(1)).saveAll(entities);
    }

    @Test
    void testGetHighestNormalizedRange_withNullDate() {
        CryptoNormalizedDto resultDto = cryptoService.getHighestNormalizedRange(null);

        assertNull(resultDto.getNormalizedRange());
        assertNull(resultDto.getSymbol());
        verify(cryptoRepository, never()).findHighestNormalizedRange(any());
    }

    @Test
    void testGetHighestNormalizedRange_withDate() {
        LocalDate date = LocalDate.now();

        CryptoNormalizedDto resultDto = cryptoService.getHighestNormalizedRange(date);

        assertNotNull(resultDto);
        verify(cryptoRepository, times(1)).findHighestNormalizedRange(any());
    }
}