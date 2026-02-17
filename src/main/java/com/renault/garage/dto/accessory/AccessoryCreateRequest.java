package com.renault.garage.dto.accessory;


import com.renault.garage.entity.AccessoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccessoryCreateRequest(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        @Positive
        BigDecimal price,
        @NotNull
        AccessoryType type,
        @NotNull
        Long vehicleId
) {}
