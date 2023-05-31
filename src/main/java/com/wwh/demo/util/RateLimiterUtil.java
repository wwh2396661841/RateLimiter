package com.wwh.demo.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class RateLimiterUtil {
    /** 默认IP */
    public static final String DEFAULT_IP = "unknown";
    /** 默认QPS */
    public static final double DEFAULT_QPS = 10d;
    /** 默认缓存个数 超出后流量自动清理 */
    private static final int DEFAULT_CACHE_COUNT = 10_0000;
    /** 默认缓存时效 超出后自动清理 */
    private static final int DEFAULT_CACHE_TIME = 5;
    /** 默认等待时长 (毫秒)*/
    private static final int DEFAULT_WAIT = 500;
    /** 限流器单机缓存 */
    private static final Cache<String, Map<String, RateLimiter>> LFU_CACHE;

    static{
        LFU_CACHE = CacheBuilder
                .newBuilder().maximumSize(DEFAULT_CACHE_COUNT)
                .expireAfterWrite(DEFAULT_CACHE_TIME, TimeUnit.MINUTES).build();
    }

    public static boolean limit(double dqs, HttpServletRequest request) throws ExecutionException {
        String ip="127.0.0.1";//测试生产环境中可以从request中获取
        String clientUri= request.getRequestURI();
        return limit(dqs,ip,clientUri);
    }
    public static boolean limit(double dqs,String ip,String clientUri) throws ExecutionException {
        Map<String, RateLimiter> stringRateLimiterMap = LFU_CACHE.get(ip, () -> {
            // 当缓存取不到时 重新加载缓存，使用线程安全的
            Map<String, RateLimiter> tmpMap = Maps.newConcurrentMap();
            // 设置限流器
//            RateLimiter rateLimiter = SmoothRateLimiterAccessor.create();
            RateLimiter rateLimiter = SmoothRateLimiterAccessor.createSmoothBursty(dqs,60.0 * 0.5);
            tmpMap.put(clientUri, rateLimiter);
            return tmpMap;
        });
        long l = SECONDS.toMicros(1L);
        RateLimiter rateLimiter = stringRateLimiterMap.get(clientUri);
        boolean flag = rateLimiter.tryAcquire(60,DEFAULT_WAIT,TimeUnit.MILLISECONDS);
        //如果令牌不够，等待DEFAULT_WAIT毫秒，够则放行
        if(flag){
            return true;
        }else {
            return false;
        }
    }
}
