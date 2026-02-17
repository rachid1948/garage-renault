package com.renault.garage.repository;

import com.renault.garage.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GarageRepositoryIT {
    @Autowired
    private GarageRepository garageRepository;
    @Test
    void search_shouldFilterByVehicleType_only() {
        // given
        GarageEntity g1 = garageRepository.save(GarageEntity.builder()
                .name("Garage A").address("addr").telephone("111").email("a@mail.com")
                .build());
        GarageEntity g2 = garageRepository.save(GarageEntity.builder()
                .name("Garage B").address("addr").telephone("222").email("b@mail.com")
                .build());
        // vehicle in g1 : CAR
        VehicleEntity v1 = VehicleEntity.builder()
                .model("Clio").brand("Renault").yearOfManufacture(2020)
                .fuelType(FuelType.DIESEL)
                .vehicleType(VehicleType.CAR)
                .garage(g1)
                .build();
        g1.getVehicles().add(v1);
        // vehicle in g2 : TRUCK
        VehicleEntity v2 = VehicleEntity.builder()
                .model("Master").brand("Renault").yearOfManufacture(2019)
                .fuelType(FuelType.DIESEL)
                .vehicleType(VehicleType.TRUCK)
                .garage(g2)
                .build();

        g2.getVehicles().add(v2);
        // resave to persist vehicles (cascade ALL in GarageEntity)
        garageRepository.saveAll(List.of(g1, g2));
        Pageable pageable = PageRequest.of(0, 10);
        // when
        var result = garageRepository.search(VehicleType.CAR, null, pageable);
        // then
        assertThat(result.getContent())
                .extracting(GarageEntity::getName)
                .containsExactly("Garage A");
    }

    @Test
    void search_shouldFilterByAccessoryName_only() {
        // given
        GarageEntity g1 = garageRepository.save(GarageEntity.builder()
                .name("Garage With GPS").address("addr").telephone("111").email("gps@mail.com")
                .build());

        GarageEntity g2 = garageRepository.save(GarageEntity.builder()
                .name("Garage Without GPS").address("addr").telephone("222").email("nogps@mail.com")
                .build());

        VehicleEntity v1 = VehicleEntity.builder()
                .model("Clio").brand("Renault").yearOfManufacture(2020)
                .fuelType(FuelType.DIESEL)
                .vehicleType(VehicleType.CAR)
                .garage(g1)
                .build();

        AccessoryEntity a1 = AccessoryEntity.builder()
                .name("GPS")
                .description("gps desc")
                .price(BigDecimal.valueOf(100))
                .vehicle(v1)
                .build();

        v1.getAccessories().add(a1);
        g1.getVehicles().add(v1);
        VehicleEntity v2 = VehicleEntity.builder()
                .model("Megane").brand("Renault").yearOfManufacture(2021)
                .fuelType(FuelType.DIESEL)
                .vehicleType(VehicleType.CAR)
                .garage(g2)
                .build();

        g2.getVehicles().add(v2);
        garageRepository.saveAll(List.of(g1, g2));
        Pageable pageable = PageRequest.of(0, 10);

        // when (test case-insensitive)
        var result = garageRepository.search(null, "gps", pageable);
        // then
        assertThat(result.getContent())
                .extracting(GarageEntity::getName)
                .containsExactly("Garage With GPS");
    }

    @Test
    void search_shouldFilterByVehicleType_andAccessoryName() {
        // given
        GarageEntity g1 = garageRepository.save(GarageEntity.builder()
                .name("Garage Combo").address("addr").telephone("111").email("combo@mail.com")
                .build());

        GarageEntity g2 = garageRepository.save(GarageEntity.builder()
                .name("Garage Other").address("addr").telephone("222").email("other@mail.com")
                .build());

        // g1 has TRUCK + GPS
        VehicleEntity v1 = VehicleEntity.builder()
                .model("Master").brand("Renault").yearOfManufacture(2019)
                .fuelType(FuelType.DIESEL)
                .vehicleType(VehicleType.TRUCK)
                .garage(g1)
                .build();

        AccessoryEntity a1 = AccessoryEntity.builder()
                .name("GPS")
                .description("gps")
                .price(BigDecimal.valueOf(100))
                .vehicle(v1)
                .build();

        v1.getAccessories().add(a1);
        g1.getVehicles().add(v1);
        // g2 has TRUCK but NO GPS
        VehicleEntity v2 = VehicleEntity.builder()
                .model("Master2").brand("Renault").yearOfManufacture(2020)
                .fuelType(FuelType.DIESEL)
                .vehicleType(VehicleType.TRUCK)
                .garage(g2)
                .build();

        g2.getVehicles().add(v2);
        garageRepository.saveAll(List.of(g1, g2));
        Pageable pageable = PageRequest.of(0, 10);

        // when

        var result = garageRepository.search(VehicleType.TRUCK, "GPS", pageable);

        // then

        assertThat(result.getContent())
                .extracting(GarageEntity::getName)
                .containsExactly("Garage Combo");
    }
}
