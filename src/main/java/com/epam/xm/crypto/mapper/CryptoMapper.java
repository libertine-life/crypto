package com.epam.xm.crypto.mapper;

import com.epam.xm.crypto.data.CryptoDto;
import com.epam.xm.crypto.data.CryptoNormalizedDto;
import com.epam.xm.crypto.data.CryptoNormalizedProjection;
import com.epam.xm.crypto.data.CryptoStatisticsDto;
import com.epam.xm.crypto.data.CryptoStatisticsProjection;
import com.epam.xm.crypto.entity.CryptoEntity;
import com.epam.xm.crypto.entity.CryptoStatisticsEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Mapper class for mapping between DTOs and entities for CryptoCurrency
 */
@Component
public class CryptoMapper {

    public CryptoEntity toEntity(CryptoDto cryptoDto) {
        CryptoEntity entity = new CryptoEntity();
        entity.setSymbol(cryptoDto.getSymbol());
        entity.setPrice(cryptoDto.getPrice());

        LocalDateTime date =
                Instant.ofEpochMilli(cryptoDto.getTimestamp())
                        .atZone(ZoneOffset.UTC)
                        .toLocalDateTime();

        entity.setPriceDate(date);
        entity.setMonth(date.getMonthValue());
        entity.setYear(date.getYear());

        return entity;
    }

    public List<CryptoStatisticsDto> toCryptoStatisticsDtosFromEntities(List<CryptoStatisticsEntity> entities) {
        return entities.stream().map(entity -> {
            CryptoStatisticsDto dto = new CryptoStatisticsDto();
            dto.setSymbol(entity.getSymbol());
            dto.setMinValue(entity.getMinValue());
            dto.setMaxValue(entity.getMaxValue());
            dto.setNewestValue(entity.getNewestValue());
            dto.setOldestValue(entity.getOldestValue());
            return dto;
        }).toList();
    }

    public List<CryptoStatisticsDto> toCryptoStatisticsDtosFromProjections(List<CryptoStatisticsProjection> projections) {
        return projections.stream().map(proj -> {
            CryptoStatisticsDto dto = new CryptoStatisticsDto();
            dto.setSymbol(proj.getSymbol());
            dto.setMinValue(proj.getMinValue());
            dto.setMaxValue(proj.getMaxValue());
            dto.setNewestValue(proj.getNewestValue());
            dto.setOldestValue(proj.getOldestValue());
            return dto;
        }).toList();
    }

    public List<CryptoNormalizedDto> toCryptoNormalizedDtos(List<CryptoNormalizedProjection> projections) {
        return projections.stream().map(proj -> toCryptoNormalizedDto(proj)).toList();
    }

    public CryptoNormalizedDto toCryptoNormalizedDto(CryptoNormalizedProjection proj) {
        CryptoNormalizedDto dto = new CryptoNormalizedDto();
        dto.setSymbol(proj.getSymbol());
        dto.setNormalizedRange(proj.getNormalizedRange());
        return dto;
    }
}

