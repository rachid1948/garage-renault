package com.renault.garage.dto.garage;

import java.util.List;

public record GarageResponse(
        Long id,
        String name,
        String address,
        String telephone,
        String email,
        List<OpeningTimeDto> openingTimes
) {}
