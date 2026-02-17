package com.renault.garage.controller;
import com.renault.garage.dto.accessory.*;
import com.renault.garage.service.AccessoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/accessories")
@RequiredArgsConstructor
public class AccessoryController {
    private final AccessoryService accessoryService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccessoryResponse create(@Valid @RequestBody AccessoryCreateRequest request) {
        return accessoryService.create(request);
    }
    @PutMapping("/{id}")
    public AccessoryResponse update(@PathVariable Long id,
                                    @Valid @RequestBody AccessoryUpdateRequest request) {
        return accessoryService.update(id, request);
    }
    @GetMapping("/vehicle/{vehicleId}")
    public List<AccessoryResponse> getByVehicle(@PathVariable Long vehicleId) {
        return accessoryService.getByVehicle(vehicleId);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        accessoryService.delete(id);
    }

    @PatchMapping("/vehicle/{vehicleId}/{id}")
    public AccessoryResponse patch(
            @PathVariable Long vehicleId,
            @PathVariable Long id,
            @RequestBody AccessoryPatchRequest request
    ) {
        return accessoryService.patch(vehicleId, id, request);
    }
}