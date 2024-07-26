package com.epam.xm.crypto.controller;

import com.epam.xm.crypto.constants.AppConstants;
import com.epam.xm.crypto.service.CsvProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * File Upload Controller which deals with file upload related endpoints
 */

@Tag(name = "File Upload Controller")
@RestController
@RequestMapping(value = AppConstants.DEFAULT_API_ROOT + "file", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class FileUploadController {

    private final CsvProcessingService csvProcessingService;

    @Operation(summary = "Method to upload a CSV file")
    @PostMapping(value = "/upload-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCSV(@Parameter(
            description = "CSV file",
            schema = @Schema(type = "string", format = "binary"))
                                            @RequestPart MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!"csv".equals(extension)) {
            throw new IllegalArgumentException("File is not a .csv");
        }

        csvProcessingService.readCsv(file);
        return ResponseEntity.ok("File uploaded and processed successfully");
    }
}
