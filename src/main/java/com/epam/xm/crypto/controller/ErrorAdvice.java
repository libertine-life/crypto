/*
 * Copyright © 2022 EPAM Systems, Inc. All Rights Reserved. All information contained herein is, and remains the
 * property of EPAM Systems, Inc. and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from EPAM Systems, Inc
 */
package com.epam.xm.crypto.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * This @ControllerAdvice class is used to handle global exceptions
 */
@Slf4j
@ControllerAdvice
public class ErrorAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorStatusResponse> handleIllegalArgumentException(HttpServletRequest request, Throwable e) {

        ErrorStatusResponse response = ErrorStatusResponse.builder()
                .error(true)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorStatusResponse> handleMaxUploadSizeExceededException(HttpServletRequest request, Throwable e) {

        ErrorStatusResponse response = ErrorStatusResponse.builder()
                .error(true)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @Data
    @NoArgsConstructor
    @SuperBuilder
    protected static class ErrorStatusResponse {
        private boolean error;
        private String message;
        private int status;
        private String path;
    }
}
