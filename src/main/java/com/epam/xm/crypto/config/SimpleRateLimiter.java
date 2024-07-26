package com.epam.xm.crypto.config;

/**
 * Implementation of a simple rate limiter.
 * The purpose of this rate limiter is to limit the number of requests
 * for IP in a given time period.
 */
public class SimpleRateLimiter {
    private final long maxRequestSize;
    private final long refillRate;

    private long currentBucketSize;
    private long lastRequestTimestamp;

    /**
     * Creates a new SimpleRateLimiter
     *
     * @param maxRequestSize the maximum count of requests
     * @param refillRateInSeconds number of seconds after which the request counter will be reset
     */
    public SimpleRateLimiter(long maxRequestSize, int refillRateInSeconds) {
        this.maxRequestSize = maxRequestSize;
        this.refillRate = refillRateInSeconds * 1000000000;
        this.currentBucketSize = maxRequestSize;
        this.lastRequestTimestamp = System.nanoTime();
    }

    /**
     * Method to request access to a resource.
     *
     * @return true if access is granted, false otherwise
     */
    public synchronized boolean tryAcquire() {
        long now = System.nanoTime();

        if (currentBucketSize == 0) {
            if (now - lastRequestTimestamp < refillRate) {
                return false;
            }
            currentBucketSize = maxRequestSize;
        }

        currentBucketSize--;
        lastRequestTimestamp = System.nanoTime();
        return true;
    }
}
