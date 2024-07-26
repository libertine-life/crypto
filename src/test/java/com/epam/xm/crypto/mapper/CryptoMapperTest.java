package com.epam.xm.crypto.mapper;

import com.epam.xm.crypto.data.CryptoDto;
import com.epam.xm.crypto.data.CryptoNormalizedDto;
import com.epam.xm.crypto.data.CryptoNormalizedProjection;
import com.epam.xm.crypto.data.CryptoStatisticsDto;
import com.epam.xm.crypto.data.CryptoStatisticsProjection;
import com.epam.xm.crypto.entity.CryptoEntity;
import com.epam.xm.crypto.entity.CryptoStatisticsEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CryptoMapperTest {

    private CryptoMapper cryptoMapper;

    @BeforeEach
    public void setUp() {
        cryptoMapper = new CryptoMapper();
    }

    @Test
    public void testToEntity() {
        CryptoDto cryptoDto = new CryptoDto();
        cryptoDto.setSymbol("BTC");
        cryptoDto.setPrice(new BigDecimal(1000));
        cryptoDto.setTimestamp(1641009600000L); // 2022-01-01T04:00

        CryptoEntity result = cryptoMapper.toEntity(cryptoDto);

        LocalDateTime expectedDate = LocalDateTime.of(2022, 1, 1, 4, 0, 0, 0);
        assertEquals(cryptoDto.getSymbol(), result.getSymbol());
        assertEquals(cryptoDto.getPrice(), result.getPrice());
        assertEquals(expectedDate, result.getPriceDate());
        assertEquals(1, result.getMonth());
        assertEquals(2022, result.getYear());
    }

    @Test
    public void testToCryptoStatisticsDtosFromEntities() {
        CryptoStatisticsEntity entity = new CryptoStatisticsEntity();
        entity.setSymbol("BTC");
        entity.setMinValue(new BigDecimal(1000));
        entity.setMaxValue(new BigDecimal(5000));
        entity.setNewestValue(new BigDecimal(3000));
        entity.setOldestValue(new BigDecimal(4000));

        List<CryptoStatisticsDto> result = cryptoMapper.toCryptoStatisticsDtosFromEntities(List.of(entity));

        assertEquals(1, result.size());
        assertEquals(entity.getSymbol(), result.get(0).getSymbol());
        assertEquals(entity.getMinValue(), result.get(0).getMinValue());
        assertEquals(entity.getMaxValue(), result.get(0).getMaxValue());
        assertEquals(entity.getNewestValue(), result.get(0).getNewestValue());
        assertEquals(entity.getOldestValue(), result.get(0).getOldestValue());
    }

    @Test
    public void testToCryptoStatisticsDtosFromProjections() {
        CryptoStatisticsProjection projection = mock(CryptoStatisticsProjection.class);
        when(projection.getSymbol()).thenReturn("BTC");
        when(projection.getMinValue()).thenReturn(new BigDecimal(1000));
        when(projection.getMaxValue()).thenReturn(new BigDecimal(5000));
        when(projection.getNewestValue()).thenReturn(new BigDecimal(2000));
        when(projection.getOldestValue()).thenReturn(new BigDecimal(4000));

        List<CryptoStatisticsDto> result = cryptoMapper.toCryptoStatisticsDtosFromProjections(List.of(projection));

        assertEquals(1, result.size());
        assertEquals(projection.getSymbol(), result.get(0).getSymbol());
        assertEquals(projection.getMinValue(), result.get(0).getMinValue());
        assertEquals(projection.getMaxValue(), result.get(0).getMaxValue());
        assertEquals(projection.getOldestValue(), result.get(0).getOldestValue());
        assertEquals(projection.getNewestValue(), result.get(0).getNewestValue());
    }

    @Test
    public void testToCryptoNormalizedDtos() {
        CryptoNormalizedProjection projection = mock(CryptoNormalizedProjection.class);
        when(projection.getSymbol()).thenReturn("BTC");
        when(projection.getNormalizedRange()).thenReturn(new BigDecimal(1000));

        List<CryptoNormalizedDto> result = cryptoMapper.toCryptoNormalizedDtos(List.of(projection));

        assertEquals(1, result.size());
        assertEquals(projection.getSymbol(), result.get(0).getSymbol());
        assertEquals(projection.getNormalizedRange(), result.get(0).getNormalizedRange());
    }

    @Test
    public void testToCryptoNormalizedDto() {
        CryptoNormalizedProjection projection = mock(CryptoNormalizedProjection.class);
        when(projection.getSymbol()).thenReturn("BTC");
        when(projection.getNormalizedRange()).thenReturn(new BigDecimal(2));

        CryptoNormalizedDto result = cryptoMapper.toCryptoNormalizedDto(projection);

        assertEquals(projection.getSymbol(), result.getSymbol());
        assertEquals(projection.getNormalizedRange(), result.getNormalizedRange());
    }
}