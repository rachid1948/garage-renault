package com.renault.garage.mapper;


import com.renault.garage.dto.garage.GarageCreateRequest;
import com.renault.garage.dto.garage.GarageResponse;
import com.renault.garage.dto.garage.OpeningTimeDto;
import com.renault.garage.entity.GarageEntity;
import com.renault.garage.entity.OpeningTimeEntity;

import java.util.List;

public class GarageMapper {
    public static GarageEntity toEntity(GarageCreateRequest request) {
        GarageEntity garage = GarageEntity.builder()
                .name(request.name())
                .address(request.address())
                .telephone(request.telephone())
                .email(request.email())
                .build();
        List<OpeningTimeEntity> openingTimes = request.openingTimes()
                .stream()
                .map(o -> OpeningTimeEntity.builder()
                        .dayOfWeek(o.dayOfWeek())
                        .startTime(o.startTime())
                        .endTime(o.endTime())
                        .garage(garage)
                        .build())
                .toList();
        garage.setOpeningTimes(openingTimes);
        return garage;
    }
    public static GarageResponse toResponse(GarageEntity entity) {
        return new GarageResponse(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getTelephone(),
                entity.getEmail(),
                entity.getOpeningTimes().stream()
                        .map(o -> new OpeningTimeDto(
                                o.getDayOfWeek(),
                                o.getStartTime(),
                                o.getEndTime()
                        ))
                        .toList()
        );
    }
}
