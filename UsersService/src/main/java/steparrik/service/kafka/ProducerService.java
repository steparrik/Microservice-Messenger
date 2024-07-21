package steparrik.service.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import steparrik.dto.user.RegistrationUserDto;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(ProducerRecord<String, String> producerRecord) {
        kafkaTemplate.send(producerRecord);
    }
}
