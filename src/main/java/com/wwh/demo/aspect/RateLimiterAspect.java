package com.wwh.demo.aspect;

import com.wwh.demo.annotation.Limit;
import com.wwh.demo.util.RateLimiterUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Component
@Aspect
public class RateLimiterAspect {
    @Pointcut("@annotation(com.wwh.demo.annotation.Limit)")
    public void requestMapping(){

    }
    @Before(value = "requestMapping()")
    public void doBefore(JoinPoint joinPoint){
        try {
            //获取当前线程的servlet请求属性，以方便获取请求的ip来源
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            if(requestAttributes!=null){
                MethodSignature signature = (MethodSignature)joinPoint.getSignature();
                //获取方法上的注解
                Limit limit = signature.getMethod().getAnnotation(Limit.class);
                double dqs = limit.dqs();
                if(RateLimiterUtil.limit(dqs,requestAttributes.getRequest())){

                }else {
                    throw new RuntimeException("限流");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
