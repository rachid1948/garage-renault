package com.renault.garage.controller;
import com.renault.garage.dto.vehicle.*;
import com.renault.garage.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse create(@Valid @RequestBody VehicleCreateRequest request) {
        return vehicleService.create(request);
    }

    @GetMapping("/{id}")
    public VehicleResponse getById(@PathVariable Long id) {
        return vehicleService.getById(id);
    }

    @PutMapping("/garage/{garageId}/{id}")
    public VehicleResponse update(
            @PathVariable Long garageId,
            @PathVariable Long id,
            @Valid @RequestBody VehicleUpdateRequest request) {
        return vehicleService.update(garageId, id, request);
    }

    @PatchMapping("/garage/{garageId}/{id}")
    public VehicleResponse patch(
            @PathVariable Long garageId,
            @PathVariable Long id,
            @RequestBody VehiclePatchRequest request
    ) {
        return vehicleService.patch(garageId, id, request);
    }


    @GetMapping("/garage/{garageId}")
    public List<VehicleResponse> getByGarage(@PathVariable Long garageId) {
        return vehicleService.getByGarage(garageId);
    }

    @DeleteMapping("/garage/{garageId}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long garageId,
            @PathVariable Long id) {
        vehicleService.delete(garageId, id);
    }

    @GetMapping("/model/{model}")
    public List<VehicleResponse> getByModel(@PathVariable String model)
    {
        return vehicleService.getByModel(model);
    }
}