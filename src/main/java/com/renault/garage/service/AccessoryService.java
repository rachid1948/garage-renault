package com.renault.garage.service;

import com.renault.garage.dto.accessory.AccessoryCreateRequest;
import com.renault.garage.dto.accessory.AccessoryPatchRequest;
import com.renault.garage.dto.accessory.AccessoryResponse;
import com.renault.garage.dto.accessory.AccessoryUpdateRequest;

import java.util.List;

public interface AccessoryService {
    AccessoryResponse create(AccessoryCreateRequest request);
    AccessoryResponse update(Long id, AccessoryUpdateRequest request);
    List<AccessoryResponse> getByVehicle(Long vehicleId);
    void delete(Long id);
    AccessoryResponse patch(Long vehicleId, Long id, AccessoryPatchRequest request);
}