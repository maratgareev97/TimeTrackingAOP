package com.timetracking.aspect;


import com.timetracking.service.TimeTrackingService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Aspect
@Component
public class TimeTrackingAspect {
    private static final Logger logger = LoggerFactory.getLogger(TimeTrackingAspect.class);

    @Autowired
    private TimeTrackingService timeTrackingService;

    @Pointcut("@annotation(com.timetracking.annotation.TrackTime)")
    public void trackTimeAnnotation() {}

    @Pointcut("@annotation(com.timetracking.annotation.TrackAsyncTime)")
    public void trackAsyncTimeAnnotation() {}

    @Around("trackTimeAnnotation()")
    public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            timeTrackingService.logExecutionTime(joinPoint.getSignature().getName(), duration);
            return result;
        } catch (Throwable throwable) {
            logger.error("Ошибка при выполнении синхронного метода: " + joinPoint.getSignature(), throwable);
            throw throwable;
        }
    }

    @Around("trackAsyncTimeAnnotation()")
    public Object trackAsyncTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        CompletableFuture<?> result = CompletableFuture.runAsync(() -> {
            try {
                joinPoint.proceed();
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                timeTrackingService.logExecutionTime(joinPoint.getSignature().getName(), duration);
            } catch (Throwable throwable) {
                logger.error("Ошибка при выполнении асинхронного метода: " + joinPoint.getSignature(), throwable);
            }
        });
        return result;
    }
}