package steparrik.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import steparrik.dto.message.MessageDTO;
import steparrik.model.chat.Chat;
import steparrik.model.message.Message;
import steparrik.model.user.User;
import steparrik.service.ChatService;
import steparrik.service.kafka.ProducerService;
import steparrik.service.MessageService;
import steparrik.service.UserService;
import steparrik.utils.exceptions.ExceptionEntity;
import steparrik.utils.mapper.message.MessageMapper;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SendMessageManager {
    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final ChatService chatService;
    private final UserService userService;
    private final ProducerService producerService;

    @Transactional
    public ResponseEntity<?> sendMessage(long id, MessageDTO messageDTO, String username) {
        Message message = new Message();
        User sender = userService.findUserByUsername(username).get();
        Optional<Chat> chat = chatService.findChatById(id);

        if(chat.isPresent()){
            if(!chat.get().getParticipants().contains(sender)){
                return new ResponseEntity<>(new ExceptionEntity("В вашем списке чатов, нет чата с данным ID", LocalDateTime.now()), HttpStatus.NOT_FOUND);
            }
            message.setMessageText(messageDTO.getMessageText());
            message.setSender(sender);
            message.setChat(chat.get());
            message.setTimestamp(LocalDateTime.now());

            messageService.save(message);

            MessageDTO messageDto = messageMapper.toDto(message);
            messageDto.setChatType(chat.get().getChatType());
            messageDto.setChatId(chat.get().getId());
            System.out.println(messageDto);
            producerService.sendMessage(messageDto);
            return ResponseEntity.ok(messageDto);
        }else{
            return new ResponseEntity<>(new ExceptionEntity("Hет чата с данным ID", LocalDateTime.now()), HttpStatus.NOT_FOUND);
        }
    }
}
