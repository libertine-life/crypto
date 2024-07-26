package com.epam.xm.crypto.controller;

import com.epam.xm.crypto.constants.AppConstants;
import com.epam.xm.crypto.data.CryptoNormalizedDto;
import com.epam.xm.crypto.data.CryptoStatisticsDto;
import com.epam.xm.crypto.service.CryptoService;
import com.epam.xm.crypto.service.CryptoStatisticsService;
import com.epam.xm.crypto.service.CryptoSymbolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller which deals with crypto information related endpoints
 */

@RestController
@Tag(name = "Crypto controller")
@RequestMapping(value = AppConstants.DEFAULT_API_ROOT + "crypto", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CryptoController {

    private final CryptoStatisticsService cryptoStatisticsService;
    private final CryptoSymbolService cryptoSymbolService;
    private final CryptoService cryptoService;

    @Operation(summary = "Returns a descending sorted list of all the cryptos,\n"
            + " comparing the normalized range (i.e. (max-min)/min)")
    @GetMapping("/normalized-range")
    public ResponseEntity<List<CryptoNormalizedDto>> getCryptoNormalized() {
        return ResponseEntity.ok(
                cryptoStatisticsService.getCryptoNormalized());
    }

    @Operation(summary = "Returns a the crypto with the highest normalized range for a specific day")
    @GetMapping("/highest-normalized-range")
    public ResponseEntity<CryptoNormalizedDto> getHighestNormalizedRange(
            @Parameter(description = "Date in format yyyy-MM-dd", example = "2022-01-01")
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(
                cryptoService.getHighestNormalizedRange(date));
    }

    @Operation(summary = "Returns the oldest/newest/min/max values for a requested crypto")
    @GetMapping("{symbol}/statistics")
    public ResponseEntity<List<CryptoStatisticsDto>> getCryptoStatistics(
            @Parameter(description = "Symbol", example = "BTC", required = true) @PathVariable String symbol,
            @Parameter(description = "Start month") @RequestParam() Integer startMonth,
            @Parameter(description = "Start year") @RequestParam() Integer startYear,
            @Parameter(description = "End month") @RequestParam() Integer endMonth,
            @Parameter(description = "End year") @RequestParam() Integer endYear) {

        if (!cryptoSymbolService.isSymbolAllowed(symbol)) {
            throw new IllegalArgumentException(String.format("Unknown symbol %s", symbol));
        }
        if (startMonth < 1 || startMonth > 12 || endMonth < 1 || endMonth > 12) {
            throw new IllegalArgumentException("Incorrect month value");
        }
        if (startYear > endYear) {
            throw new IllegalArgumentException("End year must be after start year");
        }
        if (startYear.equals(endYear) && startMonth > endMonth) {
            throw new IllegalArgumentException("End month must be after start month");
        }

        return ResponseEntity.ok(
                cryptoStatisticsService.getStatistics(symbol, startMonth, startYear, endMonth, endYear));
    }
}
