package com.renault.garage.controller;
import com.renault.garage.dto.garage.*;
import com.renault.garage.entity.VehicleType;
import com.renault.garage.service.GarageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
public class GarageController {
    private final GarageService garageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GarageResponse create(@Valid @RequestBody GarageCreateRequest request) {
        return garageService.create(request);
    }

    @GetMapping("/{id}")
    public GarageResponse getById(@PathVariable Long id) {
        return garageService.getById(id);
    }

    @PutMapping("/{id}")
    public GarageResponse update(@PathVariable Long id,
                                 @Valid @RequestBody GarageUpdateRequest request) {
        return garageService.update(id, request);
    }
    @PatchMapping("/{id}")
    public GarageResponse patch(@PathVariable Long id, @RequestBody GaragePatchRequest request) {
        return garageService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        garageService.delete(id);
    }

    @GetMapping
    public Page<GarageResponse> getAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        return garageService.getAll(pageable);
    }

    @GetMapping("/search")
    public Page<GarageResponse> search(
            @RequestParam(required = false) VehicleType vehicleType,
            @RequestParam(required = false) String accessoryName,
            @ParameterObject Pageable pageable
    ) {
        return garageService.search(vehicleType, accessoryName, pageable);
    }

}