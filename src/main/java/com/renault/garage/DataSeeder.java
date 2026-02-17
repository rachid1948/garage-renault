
package com.renault.garage;
import com.renault.garage.entity.*;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {
    private final GarageRepository garageRepository;
    private final VehicleRepository vehicleRepository;
    private final AccessoryRepository accessoryRepository;
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // si déjà seedé, on ne refait pas
        if (garageRepository.count() > 0) return;
        List<GarageEntity> garages = List.of(
                createGarage("Renault Casablanca Centre", "Casablanca - Maarif", "0522000001", "casa@renault.ma"),
                createGarage("Renault Rabat Agdal", "Rabat - Agdal", "0537000002", "rabat@renault.ma"),
                createGarage("Renault Marrakech Gueliz", "Marrakech - Gueliz", "0524000003", "marrakech@renault.ma"),
                createGarage("Renault Tanger City", "Tanger - Centre", "0539000004", "tanger@renault.ma"),
                createGarage("Renault Fes Atlas", "Fès - Route Sefrou", "0535000005", "fes@renault.ma")
        );
        // 1) on save les garages (avec horaires + listes vides)
        garageRepository.saveAll(garages);
        // 2) véhicules + accessoires (10 véhicules par garage)
        for (GarageEntity g : garages) {
            seedVehiclesForGarage(g, 10);
        }
        garageRepository.saveAll(garages);
    }
    private GarageEntity createGarage(String name, String address, String tel, String email) {
        GarageEntity g = GarageEntity.builder()
                .name(name)
                .address(address)
                .telephone(tel)
                .email(email)
                .build();
        // init lists si besoin (au cas où Builder ne les init pas)
        if (g.getVehicles() == null) g.setVehicles(new ArrayList<>());
        if (g.getOpeningTimes() == null) g.setOpeningTimes(new ArrayList<>());
        // Horaires: Lun-Ven 08:30-18:00 + Sam 09:00-13:00
        addOpeningTime(g, DayOfWeek.MONDAY, LocalTime.of(8, 30), LocalTime.of(18, 0));
        addOpeningTime(g, DayOfWeek.TUESDAY, LocalTime.of(8, 30), LocalTime.of(18, 0));
        addOpeningTime(g, DayOfWeek.WEDNESDAY, LocalTime.of(8, 30), LocalTime.of(18, 0));
        addOpeningTime(g, DayOfWeek.THURSDAY, LocalTime.of(8, 30), LocalTime.of(18, 0));
        addOpeningTime(g, DayOfWeek.FRIDAY, LocalTime.of(8, 30), LocalTime.of(18, 0));
        addOpeningTime(g, DayOfWeek.SATURDAY, LocalTime.of(9, 0), LocalTime.of(13, 0));
        return g;
    }
    private void addOpeningTime(GarageEntity garage, DayOfWeek day, LocalTime start, LocalTime end) {
        OpeningTimeEntity ot = OpeningTimeEntity.builder()
                .dayOfWeek(day)
                .startTime(start)
                .endTime(end)
                .garage(garage)
                .build();
        garage.getOpeningTimes().add(ot);
    }
    private void seedVehiclesForGarage(GarageEntity garage, int count) {
        // Modèles partagés (important pour le cas "même modèle dans plusieurs garages")
        // On va répéter ces modèles sur tous les garages.
        List<VehicleSeed> catalog = List.of(
                new VehicleSeed("Clio 5", "Renault", 2022, FuelType.GASOLINE, VehicleType.CAR),
                new VehicleSeed("Megane", "Renault", 2021, FuelType.DIESEL, VehicleType.CAR),
                new VehicleSeed("Captur", "Renault", 2023, FuelType.HYBRID, VehicleType.CAR),
                new VehicleSeed("Austral", "Renault", 2024, FuelType.HYBRID, VehicleType.CAR),
                new VehicleSeed("Duster", "Dacia", 2020, FuelType.DIESEL, VehicleType.CAR),
                new VehicleSeed("Arkana", "Renault", 2022, FuelType.GASOLINE, VehicleType.CAR),
                new VehicleSeed("Kadjar", "Renault", 2019, FuelType.DIESEL, VehicleType.CAR),
                new VehicleSeed("Talisman", "Renault", 2018, FuelType.DIESEL, VehicleType.CAR),
                new VehicleSeed("Kangoo", "Renault", 2021, FuelType.ELECTRIC, VehicleType.VAN),
                new VehicleSeed("Master", "Renault", 2020, FuelType.DIESEL, VehicleType.TRUCK)
        );
        for (int i = 0; i < count; i++) {
            VehicleSeed v = catalog.get(i % catalog.size());
            VehicleEntity vehicle = new VehicleEntity();
            vehicle.setModel(v.model);
            vehicle.setBrand(v.brand);
            vehicle.setYearOfManufacture(v.year);
            vehicle.setFuelType(v.fuel);
            vehicle.setVehicleType(v.type);
            vehicle.setGarage(garage);
            // relation bi-directionnelle
            garage.getVehicles().add(vehicle);
            // accessoires (3 par véhicule)
            addAccessory(vehicle, "GPS", "Navigation GPS intégrée", new BigDecimal("1499.00"), AccessoryType.ELECTRONICS);
            addAccessory(vehicle, "Dashcam", "Caméra embarquée HD", new BigDecimal("899.00"), AccessoryType.SAFETY);
            // 3ème accessoire varie selon type du véhicule
            if (vehicle.getVehicleType() == VehicleType.TRUCK || vehicle.getVehicleType() == VehicleType.VAN) {
                addAccessory(vehicle, "Roof Rack", "Galerie de toit renforcée", new BigDecimal("1299.00"), AccessoryType.EXTERIOR);
            } else {
                addAccessory(vehicle, "Seat Cover", "Housses siège premium", new BigDecimal("499.00"), AccessoryType.INTERIOR);
            }
        }
    }
    private void addAccessory(VehicleEntity vehicle, String name, String description, BigDecimal price, AccessoryType type) {
        AccessoryEntity a = new AccessoryEntity();
        a.setName(name);
        a.setDescription(description);
        a.setPrice(price);
        a.setType(type);
        a.setVehicle(vehicle);
        // relation bi-directionnelle
        if (vehicle.getAccessories() == null) vehicle.setAccessories(new ArrayList<>());
        vehicle.getAccessories().add(a);
    }
    private record VehicleSeed(String model, String brand, int year, FuelType fuel, VehicleType type) {}
}