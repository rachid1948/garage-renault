package com.renault.garage.mapper;


import com.renault.garage.dto.vehicle.VehicleCreateRequest;
import com.renault.garage.dto.vehicle.VehicleResponse;
import com.renault.garage.entity.VehicleEntity;

public class VehicleMapper {
    public static VehicleEntity toEntity(VehicleCreateRequest request) {
        return VehicleEntity.builder()
                .model(request.model())
                .brand(request.brand())
                .yearOfManufacture(request.yearOfManufacture())
                .fuelType(request.fuelType())
                .vehicleType(request.vehicleType())
                .build();
    }
    public static VehicleResponse toResponse(VehicleEntity entity) {
        return new VehicleResponse(
                entity.getId(),
                entity.getModel(),
                entity.getBrand(),
                entity.getYearOfManufacture(),
                entity.getFuelType(),
                entity.getVehicleType(),
                entity.getGarage().getId(),
                entity.getGarage().getName()
        );
    }
}
