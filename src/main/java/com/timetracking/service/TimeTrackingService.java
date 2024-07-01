package com.timetracking.service;

import com.timetracking.model.ExecutionStats;
import com.timetracking.repository.TimeTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TimeTrackingService {
    @Autowired
    private TimeTrackingRepository repository;

    @Async
    public void logExecutionTime(String methodName, long duration) {
        ExecutionStats stats = new ExecutionStats();
        stats.setMethodName(methodName);
        stats.setExecutionTime(duration);
        repository.save(stats);
        System.out.println("метод " + methodName + " завершен за " + duration + " ms");
    }

}
