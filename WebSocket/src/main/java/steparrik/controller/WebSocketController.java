package steparrik.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import steparrik.dto.message.MessageDTO;
import steparrik.service.rabbitmq.RabbitMQSenderService;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final RabbitMQSenderService rabbitMQSenderService;


    @MessageMapping("/chat.sendMessage/{chatId}")
    public void sendMessage(@DestinationVariable String chatId, @Payload MessageDTO messageDTO) {
        rabbitMQSenderService.send("messageQueue", messageDTO);
    }
}
