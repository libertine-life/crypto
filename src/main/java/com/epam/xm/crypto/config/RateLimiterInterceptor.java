package com.epam.xm.crypto.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    @Value("${com.epam.xm.crypto.max-request-size}")
    private Long maxRequestSize = 1000L;

    @Value("${com.epam.xm.crypto.refill-rate-in-seconds}")
    private Integer refillRateInSeconds = 10;

    private final Map<String, SimpleRateLimiter> limiters = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String uri = request.getRequestURI();
        // Skip Swagger UI endpoints from the rate limit
        if (uri.contains("swagger") || uri.startsWith("/webjars/")
                || uri.startsWith("/v3/api-docs")) {
            return true;
        }

        SimpleRateLimiter rateLimiter = getRateLimiter(request.getRemoteAddr());
        boolean allowRequest = rateLimiter.tryAcquire();

        if (!allowRequest) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        }
        return allowRequest;
    }

    private SimpleRateLimiter getRateLimiter(String key) {
        return this.limiters
                .computeIfAbsent(key,
                        k -> new SimpleRateLimiter(maxRequestSize, refillRateInSeconds));
    }

}
