package com.renault.garage.messaging.consumer;


import com.renault.garage.messaging.event.VehicleCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class VehicleCreatedConsumer {
    @KafkaListener(
            topics = "${app.kafka.topics.vehicleCreated}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(VehicleCreatedEvent event) {
        log.info("✅ RECEIVED vehicle-created event: {}", event);
    }
}
