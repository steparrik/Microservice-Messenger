package steparrik.service.rabbit;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import steparrik.dto.message.MessageDTO;

@Service
@RequiredArgsConstructor
public class RabbitMQSenderService {
    private final RabbitTemplate rabbitTemplate;

    public void send(String queueName, MessageDTO messageDTO){
        rabbitTemplate.convertAndSend(queueName, messageDTO);
    }

}