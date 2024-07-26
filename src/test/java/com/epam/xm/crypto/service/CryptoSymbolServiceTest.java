package com.epam.xm.crypto.service;

import com.epam.xm.crypto.entity.CryptoSymbolEntity;
import com.epam.xm.crypto.repository.CryptoSymbolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CryptoSymbolServiceTest {

    @Mock
    private CryptoSymbolRepository cryptoSymbolRepository;

    private CryptoSymbolService cryptoSymbolService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cryptoSymbolService = new CryptoSymbolService(cryptoSymbolRepository);
    }

    @Test
    public void testGetAllowedSymbols() {
        when(cryptoSymbolRepository.findAll()).thenReturn(Arrays.asList(
                new CryptoSymbolEntity("BTC"),
                new CryptoSymbolEntity("ETH")
        ));

        Set<String> allowedSymbols = cryptoSymbolService.getAllowedSymbols();

        assertEquals(2, allowedSymbols.size());
        assertTrue(allowedSymbols.contains("BTC"));
        assertTrue(allowedSymbols.contains("ETH"));
    }

    @Test
    public void testSymbolAllowed_WhenSymbolExists() {
        when(cryptoSymbolRepository.findById(any())).thenReturn(Optional.of(new CryptoSymbolEntity("BTC")));

        boolean exists = cryptoSymbolService.isSymbolAllowed("BTC");

        assertTrue(exists);
    }

    @Test
    public void testSymbolAllowed_WhenSymbolNotExists() {
        when(cryptoSymbolRepository.findById(any())).thenReturn(Optional.empty());

        boolean exists = cryptoSymbolService.isSymbolAllowed("BTC");

        assertFalse(exists);
    }
}