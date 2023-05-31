package com.google.common.util.concurrent;


import java.util.concurrent.TimeUnit;

public class SmoothRateLimiterAccessor {
    public static RateLimiter createSmoothWarmingUp(double permitsPerSecond, long warmupPeriod, TimeUnit timeUnit, double coldFactor) {
        RateLimiter limiter = new SmoothRateLimiter.SmoothWarmingUp(RateLimiter.SleepingStopwatch.createFromSystemTimer(), warmupPeriod, timeUnit, coldFactor);
        limiter.setRate(permitsPerSecond);
        return limiter ;
    }

    public static RateLimiter createSmoothBursty(double permitsPerSecond, double maxBurstSeconds) {
        RateLimiter limiter = new SmoothRateLimiter.SmoothBursty(RateLimiter.SleepingStopwatch.createFromSystemTimer(),maxBurstSeconds);
        limiter.setRate(permitsPerSecond);
        return limiter ;
    }
}