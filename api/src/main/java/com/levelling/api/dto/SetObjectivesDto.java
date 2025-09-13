package com.levelling.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class SetObjectivesDto {
    @NotEmpty(message = "List of IDs cannot be empty.")
    private List<String> ids;
}