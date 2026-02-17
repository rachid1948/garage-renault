package com.renault.garage.dto.vehicle;


import com.renault.garage.entity.FuelType;
import com.renault.garage.entity.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VehicleUpdateRequest(
        @NotBlank
        String model,
        @NotBlank
        String brand,
        @NotNull
        @Positive
        Integer yearOfManufacture,
        @NotNull
        FuelType fuelType,
        @NotNull
        VehicleType vehicleType
) {}