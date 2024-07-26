package com.epam.xm.crypto.controller;

import com.epam.xm.crypto.config.RateLimiterInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ControllerTest {
    @MockBean
    protected RateLimiterInterceptor rateLimiterInterceptor;

    @BeforeEach
    public void setup() throws Exception {
        when(rateLimiterInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any(Object.class)))
                .thenReturn(true);
    }
}
