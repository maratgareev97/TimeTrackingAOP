package com.timetracking.controller;

import com.timetracking.repository.TimeTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.timetracking.model.ExecutionStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TimeTrackingController {
    @Autowired
    private TimeTrackingRepository timeTrackingRepository;

    @GetMapping("/stats")
    public ResponseEntity<List<ExecutionStats>> getExecutionStats(@RequestParam String methodName) {
        List<ExecutionStats> stats = timeTrackingRepository.findByMethodName(methodName);
        if (stats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/average")
    public ResponseEntity<Double> getAverageExecutionTime(@RequestParam String methodName) {
        try {
            double average = timeTrackingRepository.findAverageExecutionTime(methodName);
            return ResponseEntity.ok(average);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalExecutionTime(@RequestParam String methodName) {
        try {
            long total = timeTrackingRepository.findTotalExecutionTime(methodName);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats/all")
    public ResponseEntity<List<Map<String, Object>>> getAllMethodStatistics() {
        List<Object[]> stats = timeTrackingRepository.findStatisticsForAllMethods();
        List<Map<String, Object>> results = new ArrayList<>();
        stats.forEach(stat -> {
            Map<String, Object> map = new HashMap<>();
            map.put("methodName", stat[0]);
            map.put("averageTime", stat[1]);
            map.put("totalTime", stat[2]);
            map.put("count", stat[3]);
            results.add(map);
        });
        return ResponseEntity.ok(results);
    }
}
