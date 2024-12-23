package steparrik.service.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import steparrik.dto.message.MessageDTO;

@Service
@RequiredArgsConstructor
public class RabbitMQListenerService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @RabbitListener(queues = "savedMessageQueue")
    public void listenMessage(MessageDTO messageDTO) {
        simpMessagingTemplate.convertAndSend("/topic/public/" + messageDTO.getChatId(), messageDTO);
    }

    @RabbitListener(queues = "notificationQueue")
    public void listenNotification(MessageDTO messageDTO) {
        simpMessagingTemplate.convertAndSend("/topic/public/notification/" + messageDTO.getChatId(), messageDTO);
        System.out.println("Notifi send on " + messageDTO.getChatId());
    }
}