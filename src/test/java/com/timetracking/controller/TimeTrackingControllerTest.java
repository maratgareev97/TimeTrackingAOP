package com.timetracking.controller;

import com.timetracking.model.ExecutionStats;
import com.timetracking.repository.TimeTrackingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TimeTrackingController.class)
public class TimeTrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeTrackingRepository timeTrackingRepository;

    @Test
    public void getExecutionStatsReturnsStats() throws Exception {
        ExecutionStats stats = new ExecutionStats();
        stats.setMethodName("testMethod");
        stats.setExecutionTime(1000);

        when(timeTrackingRepository.findByMethodName("testMethod")).thenReturn(Arrays.asList(stats));

        mockMvc.perform(get("/stats?methodName=testMethod"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].methodName").value("testMethod"))
                .andExpect(jsonPath("$[0].executionTime").value(1000));
    }

    @Test
    public void getExecutionStatsReturnsNotFound() throws Exception {
        when(timeTrackingRepository.findByMethodName("testMethod")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stats?methodName=testMethod"))
                .andExpect(status().isNotFound());
    }

}

