package com.epam.xm.crypto.controller;

import com.epam.xm.crypto.constants.AppConstants;
import com.epam.xm.crypto.service.CryptoService;
import com.epam.xm.crypto.service.CryptoStatisticsService;
import com.epam.xm.crypto.service.CryptoSymbolService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CryptoController.class)
class CryptoControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoStatisticsService cryptoStatisticsService;

    @MockBean
    private CryptoSymbolService cryptoSymbolService;

    @MockBean
    private CryptoService cryptoService;

    @Test
    void whenGetCryptoNormalized_thenReturns200() throws Exception {
        mockMvc.perform(get("/" + AppConstants.DEFAULT_API_ROOT + "crypto/normalized-range")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cryptoStatisticsService).getCryptoNormalized();
    }

    @Test
    void whenGetHighestNormalizedRange_thenReturns200() throws Exception {
        LocalDate date = LocalDate.now();
        mockMvc.perform(get("/" + AppConstants.DEFAULT_API_ROOT + "crypto/highest-normalized-range")
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cryptoService).getHighestNormalizedRange(date);
    }

    @Test
    void whenGetCryptoStatistics_thenReturns200() throws Exception {
        String symbol = "BTC";
        Integer startMonth = 1;
        Integer startYear = 2020;
        Integer endMonth = 12;
        Integer endYear = 2022;

        when(cryptoSymbolService.isSymbolAllowed(symbol)).thenReturn(true);

        mockMvc.perform(get("/" + AppConstants.DEFAULT_API_ROOT + "crypto/{symbol}/statistics", symbol)
                        .param("startMonth", startMonth.toString())
                        .param("startYear", startYear.toString())
                        .param("endMonth", endMonth.toString())
                        .param("endYear", endYear.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cryptoStatisticsService).getStatistics(symbol, startMonth, startYear, endMonth, endYear);
    }

    @Test
    void whenGetCryptoStatistics_givenWrongStartYear_thenThrowsException() throws Exception {
        String symbol = "BTC";
        Integer startMonth = 1;
        Integer startYear = 2023;
        Integer endMonth = 12;
        Integer endYear = 2022;

        when(cryptoSymbolService.isSymbolAllowed(symbol)).thenReturn(true);

        mockMvc.perform(get("/" + AppConstants.DEFAULT_API_ROOT + "crypto/{symbol}/statistics", symbol)
                        .param("startMonth", startMonth.toString())
                        .param("startYear", startYear.toString())
                        .param("endMonth", endMonth.toString())
                        .param("endYear", endYear.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 20})
    void whenGetCryptoStatistics_givenWrongStartMonth_thenThrowsException(Integer startMonth) throws Exception {
        String symbol = "BTC";
        Integer startYear = 2021;
        Integer endMonth = 12;
        Integer endYear = 2022;

        when(cryptoSymbolService.isSymbolAllowed(symbol)).thenReturn(true);

        mockMvc.perform(get("/" + AppConstants.DEFAULT_API_ROOT + "crypto/{symbol}/statistics", symbol)
                        .param("startMonth", startMonth.toString())
                        .param("startYear", startYear.toString())
                        .param("endMonth", endMonth.toString())
                        .param("endYear", endYear.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 20})
    void whenGetCryptoStatistics_givenWrongEndMonth_thenThrowsException(Integer endMonth) throws Exception {
        String symbol = "BTC";
        Integer startYear = 2021;
        Integer startMonth = 1;
        Integer endYear = 2022;

        when(cryptoSymbolService.isSymbolAllowed(symbol)).thenReturn(true);

        mockMvc.perform(get("/" + AppConstants.DEFAULT_API_ROOT + "/crypto/{symbol}/statistics", symbol)
                        .param("startMonth", startMonth.toString())
                        .param("startYear", startYear.toString())
                        .param("endMonth", endMonth.toString())
                        .param("endYear", endYear.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}