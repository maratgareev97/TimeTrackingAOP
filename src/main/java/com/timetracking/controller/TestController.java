package com.timetracking.controller;

import com.timetracking.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/testSync")
    @Operation(summary = "Синхронный метод тестирования")
    public String testSync() {
        testService.testSyncMethod();
        return "Метод синхронизации выполнен";
    }

    @GetMapping("/testAsync")
    @Operation(summary = "Асинхронный метод тестирования")
    public String testAsync() {
        testService.testAsyncMethod();
        return "Асинхронный метод выполнен";
    }
}
