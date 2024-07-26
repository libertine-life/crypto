package com.epam.xm.crypto.service;

import com.epam.xm.crypto.data.CryptoNormalizedDto;
import com.epam.xm.crypto.entity.CryptoEntity;
import com.epam.xm.crypto.mapper.CryptoMapper;
import com.epam.xm.crypto.repository.CryptoRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;


/**
 * Service handling operations related to CryptoEntity.
 */
@Service
@AllArgsConstructor
public class CryptoService {

    private final CryptoRepository cryptoRepository;
    private final CryptoMapper cryptoMapper;

    /**
     * Stores a batch of CryptoEntities.
     *
     * @param entities a collection of CryptoEntities to store.
     */
    public void saveBatch(Collection<CryptoEntity> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        cryptoRepository.saveAll(entities);
    }

    /**
     * Provides the highest normalized range for a given date.
     *
     * @param date the selected date.
     * @return the dto of type CryptoNormalizedDto.
     */
    public CryptoNormalizedDto getHighestNormalizedRange(LocalDate date) {
        if (date == null) {
            return new CryptoNormalizedDto();
        }
        return cryptoRepository.findHighestNormalizedRange(date).map(cryptoMapper::toCryptoNormalizedDto)
                .orElse(new CryptoNormalizedDto());
    }
}
