package com.renault.garage.dto.accessory;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccessoryPatchRequest(
        String name,
        String description,
        @Positive BigDecimal price,
        Boolean available
) {}