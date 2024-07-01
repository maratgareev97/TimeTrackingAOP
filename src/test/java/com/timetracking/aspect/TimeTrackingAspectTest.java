package com.timetracking.aspect;

import com.timetracking.service.TimeTrackingService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimeTrackingAspectTest {

    @InjectMocks
    private TimeTrackingAspect timeTrackingAspect;

    @Mock
    private TimeTrackingService timeTrackingService;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Signature signature;

    @Test
    public void testTrackTime() throws Throwable {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testSyncMethod"); // Указываем имя метода

        when(joinPoint.proceed()).thenReturn(null); // Предполагаем, что метод возвращает void

        timeTrackingAspect.trackTime(joinPoint);

        // Используем матчеры для всех аргументов
        verify(timeTrackingService, times(1)).logExecutionTime(eq("testSyncMethod"), anyLong());
        verify(joinPoint, times(1)).proceed();


        ArgumentCaptor<Long> durationCaptor = ArgumentCaptor.forClass(Long.class);
        verify(timeTrackingService, times(1)).logExecutionTime(eq("testSyncMethod"), durationCaptor.capture());
        assertTrue(durationCaptor.getValue() > 0); // Проверяем, что засеченное время больше 0
    }
}
