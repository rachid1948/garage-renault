package com.renault.garage.repository;

import com.renault.garage.entity.AccessoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessoryRepository extends JpaRepository<AccessoryEntity, Long> {
    List<AccessoryEntity> findByVehicleId(Long vehicleId);
    boolean existsByVehicleGarageIdAndNameIgnoreCase(Long garageId, String name);
    Optional<AccessoryEntity> findByIdAndVehicle_Id(Long id, Long vehicleId);
}
