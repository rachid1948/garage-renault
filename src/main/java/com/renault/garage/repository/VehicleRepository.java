package com.renault.garage.repository;

import com.renault.garage.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    long countByGarageId(Long garageId);
    List<VehicleEntity> findByGarageId(Long garageId);
    List<VehicleEntity> findByModelIgnoreCase(String model);
    Optional<VehicleEntity> findByIdAndGarageId(Long id, Long garageId);
}
