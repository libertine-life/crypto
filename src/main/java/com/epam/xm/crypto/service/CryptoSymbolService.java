package com.epam.xm.crypto.service;

import com.epam.xm.crypto.entity.CryptoSymbolEntity;
import com.epam.xm.crypto.repository.CryptoSymbolRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service handling operations related to CryptoSymbolEntity.
 */
@Service
@AllArgsConstructor
public class CryptoSymbolService {

    private final CryptoSymbolRepository cryptoSymbolRepository;

    /**
     * Provides a set of all allowed symbols.
     *
     * @return a set of all allowed symbols.
     */
    public Set<String> getAllowedSymbols() {
        return cryptoSymbolRepository
                .findAll()
                .stream()
                .map(CryptoSymbolEntity::getSymbol)
                .collect(Collectors.toSet());
    }

    /**
     * Check if a symbol exists in the CryptoSymbolEntity.
     *
     * @param symbol the symbol of the crypto.
     * @return true if the symbol exists, false otherwise.
     */
    public boolean isSymbolAllowed(String symbol) {
        if (symbol == null) {
            return false;
        }
        return cryptoSymbolRepository.findById(symbol).isPresent();
    }
}
