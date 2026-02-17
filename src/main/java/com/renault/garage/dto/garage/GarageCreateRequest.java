package com.renault.garage.dto.garage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record GarageCreateRequest(
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank String telephone,
        @NotBlank @Email String email,
        @NotEmpty @Valid List<OpeningTimeDto> openingTimes
) {}
