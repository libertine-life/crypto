package com.epam.xm.crypto.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleRateLimiterTest {

    private SimpleRateLimiter rateLimiter;

    @BeforeEach
    public void setUp() {
        rateLimiter = new SimpleRateLimiter(2, 1);
    }

    @Test
    public void tryAcquire_ShouldReturnTrue_WhenBucketIsNotEmpty() {
        boolean result = rateLimiter.tryAcquire();
        assertTrue(result);
    }

    @Test
    public void tryAcquire_ShouldReturnFalse_WhenBucketIsEmpty() throws InterruptedException {
        rateLimiter.tryAcquire();
        rateLimiter.tryAcquire();
        boolean result = rateLimiter.tryAcquire();
        assertFalse(result);
    }

    @Test
    public void tryAcquire_ShouldReturnTrue_WhenBucketIsRefilled() throws InterruptedException {
        rateLimiter.tryAcquire();
        rateLimiter.tryAcquire();

        Thread.sleep(1001);  // Bucket refill every second

        boolean result = rateLimiter.tryAcquire();
        assertTrue(result);
    }
}