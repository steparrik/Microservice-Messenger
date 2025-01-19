package steparrik.service.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import steparrik.client.UserClient;
import steparrik.dto.message.MessageDTO;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.chat.Chat;
import steparrik.model.message.Message;
import steparrik.service.ChatService;
import steparrik.service.MessageService;

@Service
@RequiredArgsConstructor
public class RabbitMQListenerService {
    private final MessageService messageService;
    private final UserClient userClient;
    private final ChatService chatService;
    private final RabbitMQSenderService rabbitMQSenderService;

    @RabbitListener(queues = "rawMessageQueue")
    public void listenMessage(MessageDTO messageDTO) {
        ProfileUserDto sender = userClient.getUserByUsernameOrPhoneNumber(null, messageDTO.getSender().getUsername(), null);
        Chat chat = chatService.getDefiniteChat(messageDTO.getChatId(), sender);

        MessageDTO savedMessage = messageService.sendMessage(messageDTO, sender, chat);
        rabbitMQSenderService.send("savedMessageQueue", savedMessage);
    }


}