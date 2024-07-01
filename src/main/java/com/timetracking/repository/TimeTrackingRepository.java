package com.timetracking.repository;

import com.timetracking.model.ExecutionStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeTrackingRepository extends JpaRepository<ExecutionStats, Long> {

    List<ExecutionStats> findByMethodName(String methodName);

    @Query("SELECT AVG(e.executionTime) FROM ExecutionStats e WHERE e.methodName = :methodName")
    double findAverageExecutionTime(String methodName);

    @Query("SELECT SUM(e.executionTime) FROM ExecutionStats e WHERE e.methodName = :methodName")
    long findTotalExecutionTime(String methodName);

    @Query("SELECT e.methodName, AVG(e.executionTime), SUM(e.executionTime), COUNT(e) FROM ExecutionStats e GROUP BY e.methodName")
    List<Object[]> findStatisticsForAllMethods();
}
