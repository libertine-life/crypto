package com.epam.xm.crypto.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ErrorAdviceTest extends ControllerTest {

    @Autowired
    private ErrorAdvice errorAdvice;

    @Test
    public void handleIllegalArgumentExceptionTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test-uri");

        IllegalArgumentException exception = new IllegalArgumentException("Test exception");

        ResponseEntity<ErrorAdvice.ErrorStatusResponse> responseEntity = errorAdvice
                .handleIllegalArgumentException(request, exception);

        assert responseEntity != null;
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(Objects.requireNonNull(responseEntity.getBody()).getStatus(), HttpStatus.BAD_REQUEST.value());
        assertEquals(responseEntity.getBody().getMessage(), exception.getMessage());
        assertTrue(responseEntity.getBody().isError());
        assertEquals(responseEntity.getBody().getPath(), request.getRequestURI());
    }

    @Test
    public void handleMaxUploadSizeExceededExceptionTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test-uri");

        MaxUploadSizeExceededException exception = new MaxUploadSizeExceededException(5, new Throwable());

        ResponseEntity<ErrorAdvice.ErrorStatusResponse> responseEntity = errorAdvice
                .handleMaxUploadSizeExceededException(request, exception);

        assert responseEntity != null;
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(Objects.requireNonNull(responseEntity.getBody()).getStatus(), HttpStatus.BAD_REQUEST.value());
        assertEquals(responseEntity.getBody().getMessage(), exception.getMessage());
        assertTrue(responseEntity.getBody().isError());
        assertEquals(responseEntity.getBody().getPath(), request.getRequestURI());
    }
}