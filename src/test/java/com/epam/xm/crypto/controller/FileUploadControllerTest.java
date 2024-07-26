package com.epam.xm.crypto.controller;

import com.epam.xm.crypto.constants.AppConstants;
import com.epam.xm.crypto.service.CsvProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FileUploadController.class)
class FileUploadControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CsvProcessingService csvProcessingService;

    @Test
    void whenPostUploadCSV_thenReturns200() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "content".getBytes()
        );

        mockMvc.perform(multipart("/" + AppConstants.DEFAULT_API_ROOT + "file/upload-csv")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded and processed successfully"));

        verify(csvProcessingService).readCsv(any(MultipartFile.class));
    }

    @Test
    void whenPostUploadCSV_givenEmptyFile_thenThrowsError() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "".getBytes()
        );

        mockMvc.perform(multipart("/" + AppConstants.DEFAULT_API_ROOT + "file/upload-csv")
                        .file(file))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPostUploadCSV_givenWrongExtension_thenThrowsError() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "content".getBytes()
        );

        mockMvc.perform(multipart("/" + AppConstants.DEFAULT_API_ROOT + "file/upload-csv")
                        .file(file))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPostUploadCSV_givenNoExtension_thenThrowsError() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "content".getBytes()
        );

        mockMvc.perform(multipart("/" + AppConstants.DEFAULT_API_ROOT + "file/upload-csv")
                        .file(file))
                .andExpect(status().isBadRequest());
    }
}