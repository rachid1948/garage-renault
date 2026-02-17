package com.renault.garage.service;

import com.renault.garage.dto.vehicle.VehicleCreateRequest;
import com.renault.garage.dto.vehicle.VehiclePatchRequest;
import com.renault.garage.dto.vehicle.VehicleResponse;
import com.renault.garage.dto.vehicle.VehicleUpdateRequest;

import java.util.List;

public interface VehicleService {
    VehicleResponse create(VehicleCreateRequest request);
    VehicleResponse update(Long garageId, Long id, VehicleUpdateRequest request);
    void delete(Long garageId, Long id);
    VehicleResponse getById(Long id);
    List<VehicleResponse> getByGarage(Long garageId);
    List<VehicleResponse> getByModel(String model);
    VehicleResponse patch(Long garageId, Long id, VehiclePatchRequest request);


}