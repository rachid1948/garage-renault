package com.renault.garage.messaging.event;
import java.time.Instant;

public record VehicleCreatedEvent(
        Long vehicleId,
        String brand,
        String model,
        Instant createdAt
) {}
