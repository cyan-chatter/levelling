package com.levelling.api.controllers;

import com.levelling.api.services.GemConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private GemConversionService gemConversionService;

    @PostMapping("/test/convert-daily")
    public void testDailyConversion() {
        gemConversionService.convertDailyPointsToGems();
    }

    @PostMapping("/test/convert-weekly")
    public void testWeeklyConversion() {
        gemConversionService.convertWeeklyPointsToGems();
    }

    @PostMapping("/test/convert-monthly")
    public void testMonthlyConversion() {
        gemConversionService.convertMonthlyPointsToGems();
    }
}