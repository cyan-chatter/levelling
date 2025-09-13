package com.levelling.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CompleteArcDto {
    @Min(-10)
    @Max(10)
    private int happinessLevel;

    @Min(-10)
    @Max(10)
    private int effortLevel;
}