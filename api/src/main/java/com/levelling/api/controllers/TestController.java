package com.levelling.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.levelling.api.services.GemConversionService;

@RestController
public class TestController {
    @Autowired
    private GemConversionService gemConversionService;

    @PostMapping("/test/convert-daily")
    public void testDailyConversion() {
        gemConversionService.convertDailyPointsToGems();
    }
}
