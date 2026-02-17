package com.renault.garage.dto.vehicle;


import com.renault.garage.entity.FuelType;
import com.renault.garage.entity.VehicleType;

public record VehicleResponse(
        Long id,
        String model,
        String brand,
        Integer yearOfManufacture,
        FuelType fuelType,
        VehicleType vehicleType,
        Long garageId,
        String nameGarage
) {}