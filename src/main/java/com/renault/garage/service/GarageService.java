package com.renault.garage.service;

import com.renault.garage.dto.garage.GarageCreateRequest;
import com.renault.garage.dto.garage.GaragePatchRequest;
import com.renault.garage.dto.garage.GarageResponse;
import com.renault.garage.dto.garage.GarageUpdateRequest;
import com.renault.garage.entity.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GarageService {
    GarageResponse create(GarageCreateRequest request);
    GarageResponse update(Long id, GarageUpdateRequest request);
    GarageResponse getById(Long id);
    List<GarageResponse> getAll();
    void delete(Long id);
    GarageResponse patch(Long id, GaragePatchRequest request);

    Page<GarageResponse> getAll(Pageable pageable);
    Page<GarageResponse> search(VehicleType vehicleType,
                                String accessoryName,
                                Pageable pageable);
}
