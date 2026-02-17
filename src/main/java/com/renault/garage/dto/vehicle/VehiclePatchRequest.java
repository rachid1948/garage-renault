package com.renault.garage.dto.vehicle;
import com.renault.garage.entity.FuelType;
import com.renault.garage.entity.VehicleType;
import jakarta.validation.constraints.Positive;
public record VehiclePatchRequest(
        String model,
        String brand,
        @Positive Integer yearOfManufacture,
        FuelType fuelType,
        VehicleType vehicleType
) {}
