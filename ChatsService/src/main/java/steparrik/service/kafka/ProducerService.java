package steparrik.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import steparrik.dto.message.MessageDTO;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void sendMessage(MessageDTO message) {
        kafkaTemplate.send("message-topic", message);
    }
}
