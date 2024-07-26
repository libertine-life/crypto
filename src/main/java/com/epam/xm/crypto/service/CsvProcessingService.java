package com.epam.xm.crypto.service;

import com.epam.xm.crypto.data.CryptoDto;
import com.epam.xm.crypto.entity.CryptoEntity;
import com.epam.xm.crypto.entity.CryptoStatisticsKey;
import com.epam.xm.crypto.mapper.CryptoMapper;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service for handling operations related to reading and processing CSV data.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CsvProcessingService {

    private final CryptoService cryptoService;
    private final CryptoStatisticsService cryptoStatisticsService;
    private final CryptoSymbolService cryptoSymbolService;
    private final CryptoMapper cryptoMapper;
    @Value("${com.epam.xm.crypto.batch-size}")
    private Integer batchSize = 1000;

    /**
     * Reads the CSV from the provided file, processes the data and saves it in the database.
     *
     * @param file the CSV file.
     * @throws Exception if an error occurs during the file processing.
     */
    @Transactional
    public void readCsv(MultipartFile file) throws Exception {

        Set<String> allowedSymbols = cryptoSymbolService.getAllowedSymbols();
        File tempFile = createTempFile(file);

        try {
            CsvToBean<CryptoDto> csvToBean = getCsvToBean(tempFile);

            List<CryptoEntity> batch = new ArrayList<>();

            // Set needed to understand which ranges need to be recalculated after adding new data
            Set<CryptoStatisticsKey> calculationRanges = new HashSet<>();

            csvToBean.stream().forEach(dto -> {

                checkAllowedSymbol(allowedSymbols, dto.getSymbol());

                CryptoEntity cryptoEntity = cryptoMapper.toEntity(dto);
                calculationRanges.add(new CryptoStatisticsKey(cryptoEntity.getSymbol(), cryptoEntity.getMonth(), cryptoEntity.getYear()));
                batch.add(cryptoEntity);

                if (batch.size() >= batchSize) {
                    cryptoService.saveBatch(batch);
                    batch.clear();
                }
            });

            // save the remaining items
            if (!batch.isEmpty()) {
                cryptoService.saveBatch(batch);
            }

            cryptoStatisticsService.recalculateStatistics(calculationRanges);
        } catch (Exception ex) {
            log.error("Failed to read and process CSV file", ex);
            throw ex;
        } finally {
            tempFile.delete();
        }
    }

    /**
     * Checks if the passed symbol is allowed.
     * @param allowedSymbols Set of allowed symbols
     * @param symbol Symbol to check
     */
    private static void checkAllowedSymbol(Set<String> allowedSymbols, String symbol) {
        if (!allowedSymbols.contains(symbol)) {
            String errorMsg = String.format("Unknown crypto symbol %s in the batch", symbol);
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    /**
     * Creates a temporary file from the supplied multipart file
     * @param file Multipart file to create the temporary file from
     * @throws IOException if an I/O error occurs
     * @return The created temporary file
     */
    private static File createTempFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        return tempFile;
    }

    /**
     * Creates a CsvToBean instance from the supplied file
     * @param file File to create the CsvToBean instance from
     * @throws FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     * @return CsvToBean instance
     */
    private static CsvToBean<CryptoDto> getCsvToBean(File file) throws FileNotFoundException {
        HeaderColumnNameMappingStrategy strategy = new HeaderColumnNameMappingStrategy<CryptoDto>();
        strategy.setType(CryptoDto.class);

        Reader reader = new FileReader(file);

        CsvToBean<CryptoDto> csvToBean = new CsvToBeanBuilder<CryptoDto>(reader)
                .withMappingStrategy(strategy)
                .withSeparator(',')
                .build();
        return csvToBean;
    }


}
