package com.timetracking.service;

import com.timetracking.model.ExecutionStats;
import com.timetracking.repository.TimeTrackingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
public class TimeTrackingServiceTest {

    @InjectMocks
    private TimeTrackingService timeTrackingService;

    @Mock
    private TimeTrackingRepository repository;

    @Test
    public void logExecutionTimeTest() {
        String methodName = "testMethod";
        long duration = 100L;

        timeTrackingService.logExecutionTime(methodName, duration);

        ArgumentCaptor<ExecutionStats> captor = ArgumentCaptor.forClass(ExecutionStats.class);
        verify(repository, times(1)).save(captor.capture());

        ExecutionStats capturedStats = captor.getValue();
        assertEquals(methodName, capturedStats.getMethodName());
        assertEquals(duration, capturedStats.getExecutionTime());
    }
}
