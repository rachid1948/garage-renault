package com.renault.garage.dto.accessory;


import com.renault.garage.entity.AccessoryType;

import java.math.BigDecimal;

public record AccessoryResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        AccessoryType type,
        Long vehicleId
) {}
