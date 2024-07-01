package com.timetracking.service;

import com.timetracking.annotation.TrackAsyncTime;
import com.timetracking.annotation.TrackTime;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @TrackTime
    public void testSyncMethod() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @TrackAsyncTime
    @Async
    public void testAsyncMethod() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
