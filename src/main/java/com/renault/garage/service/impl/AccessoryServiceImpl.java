package com.renault.garage.service.impl;

import com.renault.garage.dto.accessory.*;
import com.renault.garage.entity.AccessoryEntity;
import com.renault.garage.entity.VehicleEntity;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.service.AccessoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class AccessoryServiceImpl implements AccessoryService {
    private final AccessoryRepository accessoryRepository;
    private final VehicleRepository vehicleRepository;
    @Override
    public AccessoryResponse create(AccessoryCreateRequest request) {
        VehicleEntity vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));
        AccessoryEntity entity = AccessoryMapper.toEntity(request);
        entity.setVehicle(vehicle);
        return AccessoryMapper.toResponse(accessoryRepository.save(entity));
    }
    @Override
    public AccessoryResponse update(Long id, AccessoryUpdateRequest request) {
        AccessoryEntity entity = accessoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Accessory not found"));
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setPrice(request.price());
        entity.setType(request.type());
        return AccessoryMapper.toResponse(accessoryRepository.save(entity));
    }
    @Override
    public List<AccessoryResponse> getByVehicle(Long vehicleId) {
        return accessoryRepository.findByVehicleId(vehicleId)
                .stream()
                .map(AccessoryMapper::toResponse)
                .toList();
    }
    @Override
    public void delete(Long id) {
        if (!accessoryRepository.existsById(id)) {
            throw new NotFoundException("Accessory not found");
        }
        accessoryRepository.deleteById(id);
    }

    @Override
    public AccessoryResponse patch(Long vehicleId, Long id, AccessoryPatchRequest request) {
        AccessoryEntity entity = accessoryRepository
                .findByIdAndVehicle_Id(id, vehicleId)
                .orElseThrow(() -> new NotFoundException("Accessory not found in this vehicle"));
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.price() != null) entity.setPrice(request.price());
        return AccessoryMapper.toResponse(accessoryRepository.save(entity));
    }
}