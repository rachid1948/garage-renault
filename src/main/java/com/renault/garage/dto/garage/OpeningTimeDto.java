package com.renault.garage.dto.garage;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record OpeningTimeDto(
        @Schema(example = "MONDAY")
        DayOfWeek dayOfWeek,
        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(type = "string", example = "08:00:00")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(type = "string", example = "18:00:00")
        LocalTime endTime
) {}

