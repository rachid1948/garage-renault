package com.renault.garage.service.impl;


import com.renault.garage.dto.vehicle.VehicleCreateRequest;
import com.renault.garage.dto.vehicle.VehiclePatchRequest;
import com.renault.garage.dto.vehicle.VehicleResponse;
import com.renault.garage.dto.vehicle.VehicleUpdateRequest;
import com.renault.garage.entity.GarageEntity;
import com.renault.garage.entity.VehicleEntity;
import com.renault.garage.exception.VehicleQuotaExceededException;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.messaging.event.VehicleCreatedEvent;
import com.renault.garage.messaging.producer.VehicleEventProducer;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.service.VehicleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;
    private final VehicleEventProducer vehicleEventProducer;

    private static final int MAX_VEHICLES = 50;
    @Override
    public VehicleResponse create(VehicleCreateRequest request) {
        GarageEntity garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new NotFoundException("Garage not found"));
        long count = vehicleRepository.countByGarageId(garage.getId());
        if (count >= MAX_VEHICLES) {
            throw new VehicleQuotaExceededException(garage.getId(), MAX_VEHICLES);
        }
        VehicleEntity entity = VehicleMapper.toEntity(request);
        entity.setGarage(garage);
        VehicleEntity saved = vehicleRepository.save(entity);
        // Publish Kafka event AFTER save
        vehicleEventProducer.publishVehicleCreated(
                new VehicleCreatedEvent(
                        saved.getId(),
                        saved.getModel(),
                        saved.getBrand(),
                        Instant.now()
                )
        );
        return VehicleMapper.toResponse(saved);
    }
    @Override
    public VehicleResponse update(Long garageId, Long id, VehicleUpdateRequest request) {
        VehicleEntity entity = vehicleRepository
                .findByIdAndGarageId(id, garageId)
                .orElseThrow(() -> new NotFoundException("Vehicle not found in this garage"));
        entity.setModel(request.model());
        entity.setBrand(request.brand());
        entity.setYearOfManufacture(request.yearOfManufacture());
        entity.setFuelType(request.fuelType());
        entity.setVehicleType(request.vehicleType());
        return VehicleMapper.toResponse(vehicleRepository.save(entity));
    }
    @Override
    public VehicleResponse getById(Long id) {
        return vehicleRepository.findById(id)
                .map(VehicleMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));
    }
    @Override
    public List<VehicleResponse> getByGarage(Long garageId) {
        return vehicleRepository.findByGarageId(garageId)
                .stream()
                .map(VehicleMapper::toResponse)
                .toList();
    }

    @Override
    public List<VehicleResponse> getByModel(String model) {
        return vehicleRepository.findByModelIgnoreCase(model)
                .stream()
                .map(VehicleMapper::toResponse)
                .toList();
    }

    @Override
    public VehicleResponse patch(Long garageId, Long id, VehiclePatchRequest request) {
        VehicleEntity entity = vehicleRepository
                .findByIdAndGarageId(id, garageId)
                .orElseThrow(() -> new NotFoundException("Vehicle not found in this garage"));
        if (request.model() != null) entity.setModel(request.model());
        if (request.brand() != null) entity.setBrand(request.brand());
        if (request.yearOfManufacture() != null) entity.setYearOfManufacture(request.yearOfManufacture());
        if (request.fuelType() != null) entity.setFuelType(request.fuelType());
        if (request.vehicleType() != null) entity.setVehicleType(request.vehicleType());
        return VehicleMapper.toResponse(vehicleRepository.save(entity));
    }

    @Override
    public void delete(Long garageId, Long id) {
        VehicleEntity entity = vehicleRepository
                .findByIdAndGarageId(id, garageId)
                .orElseThrow(() -> new NotFoundException("Vehicle not found in this garage"));
        vehicleRepository.delete(entity);
    }

}
