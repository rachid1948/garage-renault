package com.renault.garage.mapper;

import com.renault.garage.dto.accessory.AccessoryCreateRequest;
import com.renault.garage.dto.accessory.AccessoryResponse;
import com.renault.garage.entity.AccessoryEntity;

public class AccessoryMapper {
    public static AccessoryEntity toEntity(AccessoryCreateRequest request) {
        return AccessoryEntity.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .type(request.type())
                .build();
    }
    public static AccessoryResponse toResponse(AccessoryEntity entity) {
        return new AccessoryResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getType(),
                entity.getVehicle().getId()
        );
    }
}
