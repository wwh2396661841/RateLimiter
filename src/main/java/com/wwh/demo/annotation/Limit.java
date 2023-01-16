package com.wwh.demo.annotation;

import com.wwh.demo.util.RateLimiterUtil;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limit {
    double dqs() default RateLimiterUtil.DEFAULT_QPS;
}
