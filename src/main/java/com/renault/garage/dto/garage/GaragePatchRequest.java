package com.renault.garage.dto.garage;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
public record GaragePatchRequest(
        @Schema(example = "Renault Casa Center")
        String name,
        @Schema(example = "Boulevard Zerktouni, Casablanca")
        String address,
        @Schema(example = "0522000000")
        String telephone,
        @Schema(example = "casa@renault.com")
        String email,
        List<OpeningTimeDto> openingTimes
) {}