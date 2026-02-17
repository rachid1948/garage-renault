package com.renault.garage.service.impl;

import com.renault.garage.dto.garage.*;
import com.renault.garage.entity.GarageEntity;
import com.renault.garage.entity.OpeningTimeEntity;
import com.renault.garage.entity.VehicleType;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.service.GarageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class GarageServiceImpl implements GarageService {
    private final GarageRepository garageRepository;
    @Override
    public GarageResponse create(GarageCreateRequest request) {
        GarageEntity entity = GarageMapper.toEntity(request);
        return GarageMapper.toResponse(garageRepository.save(entity));
    }
    @Override
    public GarageResponse update(Long id, GarageUpdateRequest request) {
        GarageEntity entity = garageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Garage not found"));
        entity.setName(request.name());
        entity.setAddress(request.address());
        entity.setTelephone(request.telephone());
        entity.setEmail(request.email());
        entity.getOpeningTimes().clear();
        List<OpeningTimeEntity> newOpeningTimes = request.openingTimes()
                .stream()
                .map(o -> OpeningTimeEntity.builder()
                        .dayOfWeek(o.dayOfWeek())
                        .startTime(o.startTime())
                        .endTime(o.endTime())
                        .garage(entity)
                        .build())
                .toList();
        entity.getOpeningTimes().addAll(newOpeningTimes);
        return GarageMapper.toResponse(garageRepository.save(entity));
    }
    @Override
    public GarageResponse getById(Long id) {
        return garageRepository.findById(id)
                .map(GarageMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Garage not found"));
    }
    @Override
    public List<GarageResponse> getAll() {
        return garageRepository.findAll()
                .stream()
                .map(GarageMapper::toResponse)
                .toList();
    }
    @Override
    public void delete(Long id) {
        if (!garageRepository.existsById(id)) {
            throw new NotFoundException("Garage not found");
        }
        garageRepository.deleteById(id);
    }

    public GarageResponse patch(Long id, GaragePatchRequest req) {
        GarageEntity g = garageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Garage not found"));
        if (req.name() != null) g.setName(req.name());
        if (req.address() != null) g.setAddress(req.address());
        if (req.telephone() != null) g.setTelephone(req.telephone());
        if (req.email() != null) g.setEmail(req.email());
        // openingTimes: soit tu ignores si null, soit tu remplaces si fourni
        if (req.openingTimes() != null) {
            // stratégie simple: remplacer toute la liste
            g.getOpeningTimes().clear();
            req.openingTimes().forEach(ot -> {
                OpeningTimeEntity e = new OpeningTimeEntity();
                e.setDayOfWeek(ot.dayOfWeek());
                e.setStartTime(ot.startTime());
                e.setEndTime(ot.endTime());
                e.setGarage(g);
                g.getOpeningTimes().add(e);
            });
        }
        return GarageMapper.toResponse(garageRepository.save(g));
    }

    @Override
    public Page<GarageResponse> getAll(Pageable pageable) {
        return garageRepository.findAll(pageable)
                .map(GarageMapper::toResponse);
    }

    @Override
    public Page<GarageResponse> search(VehicleType vehicleType, String accessoryName, Pageable pageable) {
        return garageRepository.search(vehicleType, accessoryName, pageable)
                .map(GarageMapper::toResponse);
    }
}