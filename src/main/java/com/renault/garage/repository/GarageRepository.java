package com.renault.garage.repository;

import com.renault.garage.entity.GarageEntity;
import com.renault.garage.entity.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GarageRepository extends JpaRepository<GarageEntity, Long>, JpaSpecificationExecutor<GarageEntity> {
    @Query("""
   SELECT DISTINCT g FROM GarageEntity g
   JOIN g.vehicles v
   LEFT JOIN v.accessories a
   WHERE (:vehicleType IS NULL OR v.vehicleType = :vehicleType)
     AND (:accessoryName IS NULL OR LOWER(a.name) = LOWER(:accessoryName))
""")
    Page<GarageEntity> search(
            @Param("vehicleType") VehicleType vehicleType,
            @Param("accessoryName") String accessoryName,
            Pageable pageable
    );
}
