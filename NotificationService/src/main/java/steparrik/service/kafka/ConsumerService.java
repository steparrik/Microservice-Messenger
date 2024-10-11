package steparrik.service.kafka;


import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import steparrik.dto.message.MessageDTO;
import steparrik.service.rabbitmq.RabbitMQSenderService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {
    private final RabbitMQSenderService rabbitMQSenderService;

    @KafkaListener(topics = "message-topic", groupId = "A")
    public void listen(MessageDTO messageDto) {
        rabbitMQSenderService.send("notificationQueue", messageDto);
    }
}
