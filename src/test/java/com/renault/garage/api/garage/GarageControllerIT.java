package com.renault.garage.api.garage;


import com.renault.garage.entity.*;
import com.renault.garage.messaging.producer.VehicleEventProducer;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * IT (Integration Test) sur l'API /api/garages/search
 * <p>
 * - filtre par vehicleType
 * <p>
 * - filtre par accessoryName (case-insensitive)
 * <p>
 * - filtre par les deux
 * <p>
 * - pagination
 */

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class GarageControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    GarageRepository garageRepository;
    @Autowired
    VehicleRepository vehicleRepository;
    @MockBean
    private VehicleEventProducer vehicleEventProducer;
    @MockBean
    private org.springframework.kafka.core.KafkaTemplate<String, com.renault.garage.messaging.event.VehicleCreatedEvent> kafkaTemplate;
    @MockBean

    private org.springframework.kafka.core.ProducerFactory<String, Object> producerFactory;

    private GarageEntity gCar;
    private GarageEntity gTruck;
    private GarageEntity gVan;

    @BeforeEach
    void setup() {
        vehicleRepository.deleteAll();
        garageRepository.deleteAll();
        gCar = garageRepository.save(GarageEntity.builder()
                .name("Garage CAR")
                .address("Addr1")
                .telephone("111")
                .email("car@mail.com")
                .build());

        gTruck = garageRepository.save(GarageEntity.builder()
                .name("Garage TRUCK")
                .address("Addr2")
                .telephone("222")
                .email("truck@mail.com")
                .build());

        gVan = garageRepository.save(GarageEntity.builder()
                .name("Garage VAN")
                .address("Addr3")
                .telephone("333")
                .email("van@mail.com")
                .build());

        // --- VEHICLES + ACCESSORIES ---

        // gCar: CAR + accessory "GPS"

        saveVehicleWithAccessory(gCar, VehicleType.CAR, FuelType.GASOLINE, "GPS", AccessoryType.ELECTRONICS);

        // gTruck: TRUCK + accessory "Camera"

        saveVehicleWithAccessory(gTruck, VehicleType.TRUCK, FuelType.DIESEL, "Camera", AccessoryType.SAFETY);

        // gVan: VAN + accessory "gps" (lowercase) pour tester le case-insensitive

        saveVehicleWithAccessory(gVan, VehicleType.VAN, FuelType.HYBRID, "gps", AccessoryType.ELECTRONICS);

    }

    @Test
    void search_shouldFilterByVehicleType_only() throws Exception {
        mockMvc.perform(get("/api/garages/search")
                        .param("vehicleType", "CAR")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Garage CAR")));
    }

    @Test
    void search_shouldFilterByAccessoryName_only_caseInsensitive() throws Exception {
        mockMvc.perform(get("/api/garages/search")
                        .param("accessoryName", "GPS") // doit matcher "GPS" + "gps"
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[*].name",
                        containsInAnyOrder("Garage CAR", "Garage VAN", "Garage TRUCK")));

    }

    @Test
    void search_shouldFilterByVehicleType_andAccessoryName() throws Exception {
        mockMvc.perform(get("/api/garages/search")
                        .param("vehicleType", "VAN")
                        .param("accessoryName", "GPS")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Garage VAN")));

    }

    @Test
    void search_shouldReturnAll_whenNoFilters() throws Exception {
        mockMvc.perform(get("/api/garages/search")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[*].name",
                        containsInAnyOrder("Garage CAR", "Garage TRUCK", "Garage VAN")));

    }

    @Test
    void search_shouldPaginate() throws Exception {
        // size=2 -> 2 résultats sur la première page
        mockMvc.perform(get("/api/garages/search")
                        .param("page", "0")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.size", is(2)))
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.totalPages", is(2)));

        // page=1 -> le dernier résultat

        mockMvc.perform(get("/api/garages/search")
                        .param("page", "1")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.size", is(2)))
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.totalPages", is(2)));
    }

    // -----------------------

    // Helpers

    // -----------------------

    private VehicleEntity saveVehicleWithAccessory(
            GarageEntity garage,
            VehicleType vehicleType,
            FuelType fuelType,
            String accessoryName,
            AccessoryType accessoryType
    ) {

        VehicleEntity v = VehicleEntity.builder()
                .model("Model-" + vehicleType.name())
                .brand("Brand-" + vehicleType.name())
                .yearOfManufacture(2022)
                .fuelType(fuelType)
                .vehicleType(vehicleType)
                .garage(garage)
                .build();

        if (v.getAccessories() == null) {
            v.setAccessories(new ArrayList<>());
        }

        AccessoryEntity a = AccessoryEntity.builder()
                .name("GPS")
                .description("GPS intégré")
                .type(AccessoryType.ELECTRONICS)
                .price(BigDecimal.valueOf(1500))   // OBLIGATOIRE
                .vehicle(v)
                .build();

        v.getAccessories().add(a);
        return vehicleRepository.save(v);

    }

}
