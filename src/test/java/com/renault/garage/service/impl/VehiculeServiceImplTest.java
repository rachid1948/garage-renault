package com.renault.garage.service.impl;
import com.renault.garage.dto.vehicle.VehicleCreateRequest;
import com.renault.garage.dto.vehicle.VehiclePatchRequest;
import com.renault.garage.entity.FuelType;
import com.renault.garage.entity.GarageEntity;
import com.renault.garage.entity.VehicleEntity;
import com.renault.garage.entity.VehicleType;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.exception.VehicleQuotaExceededException;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {
    @Mock VehicleRepository vehicleRepository;
    @Mock GarageRepository garageRepository;
    @InjectMocks VehicleServiceImpl service;
    @Test
    void create_shouldThrowNotFound_whenGarageDoesNotExist() {
        long garageId = 10L;
        when(garageRepository.findById(garageId)).thenReturn(Optional.empty());
        var req = new VehicleCreateRequest("Clio", "Renault", 2020,
                FuelType.DIESEL, VehicleType.CAR, garageId);
        assertThrows(NotFoundException.class, () -> service.create(req));
        verify(vehicleRepository, never()).save(any());
    }
    @Test
    void create_shouldThrowQuotaExceeded_whenGarageAlreadyHas50Vehicles() {
        long garageId = 10L;
        when(garageRepository.findById(garageId))
                .thenReturn(Optional.of(GarageEntity.builder().id(garageId).name("G1").build()));
        when(vehicleRepository.countByGarageId(garageId)).thenReturn(50L);
        var req = new VehicleCreateRequest("Clio", "Renault", 2020,
                FuelType.DIESEL, VehicleType.CAR, garageId);
        assertThrows(VehicleQuotaExceededException.class, () -> service.create(req));
        verify(vehicleRepository, never()).save(any());
    }
    @Test
    void create_shouldSave_whenQuotaNotReached() {
        long garageId = 10L;
        var garage = GarageEntity.builder().id(garageId).name("G1").build();
        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));
        when(vehicleRepository.countByGarageId(garageId)).thenReturn(12L);
        when(vehicleRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        var req = new VehicleCreateRequest("Clio", "Renault", 2020,
                FuelType.DIESEL, VehicleType.CAR, garageId);
        var res = service.create(req);
        assertNotNull(res);
        verify(vehicleRepository).save(any(VehicleEntity.class));
    }
    @Test
    void patch_shouldUpdateOnlyProvidedFields() {
        long garageId = 1L;
        long vehicleId = 2L;
        var existing = VehicleEntity.builder()
                .id(vehicleId)
                .model("Old")
                .brand("OldBrand")
                .yearOfManufacture(2010)
                .fuelType(FuelType.DIESEL)
                .vehicleType(VehicleType.CAR)
                .garage(GarageEntity.builder().id(garageId).name("G").build())
                .build();
        when(vehicleRepository.findByIdAndGarageId(vehicleId, garageId)).thenReturn(Optional.of(existing));
        when(vehicleRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        var patch = new VehiclePatchRequest("NewModel", null, null, null, null);
        var res = service.patch(garageId, vehicleId, patch);
        assertEquals("NewModel", existing.getModel());
        assertEquals("OldBrand", existing.getBrand()); // unchanged
        verify(vehicleRepository).save(existing);
        assertNotNull(res);
    }
    @Test
    void delete_shouldThrowNotFound_whenVehicleNotInGarage() {
        long garageId = 1L;
        long vehicleId = 2L;
        when(vehicleRepository.findByIdAndGarageId(vehicleId, garageId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.delete(garageId, vehicleId));
        verify(vehicleRepository, never()).delete(any());
    }
    @Test
    void delete_shouldDelete_whenVehicleExistsInGarage() {
        long garageId = 1L;
        long vehicleId = 2L;
        var existing = VehicleEntity.builder()
                .id(vehicleId)
                .garage(GarageEntity.builder().id(garageId).name("G").build())
                .build();
        when(vehicleRepository.findByIdAndGarageId(vehicleId, garageId)).thenReturn(Optional.of(existing));
        service.delete(garageId, vehicleId);
        verify(vehicleRepository).delete(existing);
    }
}