package com.epam.xm.crypto.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for MVC-related settings.
 * This includes setting up the Interceptors for rate limiting.
 */

@Configuration
@AllArgsConstructor
public class MvcConfig implements WebMvcConfigurer {

    private final RateLimiterInterceptor rateLimiterInterceptor;

    /**
     * Adds RateLimiterInterceptor to global
     * interceptor registry.
     *
     * @param registry registry of MVC interceptors
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimiterInterceptor);
    }
}
