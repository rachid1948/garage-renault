package com.renault.garage.messaging.producer;
import com.renault.garage.messaging.event.VehicleCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VehicleEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;
    public VehicleEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topics.vehicleCreated}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }
    public void publishVehicleCreated(VehicleCreatedEvent event) {
        if (event == null) {
            log.warn("VehicleCreatedEvent is null -> nothing to publish");
            return;
        }
        String key = event.vehicleId() == null ? null : String.valueOf(event.vehicleId());
        log.info(">>>>>>>> SENDING EVENT TO TOPIC: {} | key={}", topic, key);
        log.info(">>>>>>>> EVENT PAYLOAD: {}", event);
        kafkaTemplate.send(topic, key, event)
                .whenComplete((SendResult<String, Object> result, Throwable ex) -> {
                    if (ex != null) {
                        log.error("!!!!!!!! FAILED to send event to topic {} | key={}", topic, key, ex);
                        return;
                    }
                    if (result != null && result.getRecordMetadata() != null) {
                        log.info("<<<<<<<< SENT OK topic={} partition={} offset={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.info("<<<<<<<< SENT OK topic={} (no metadata)", topic);
                    }
                });
    }
}