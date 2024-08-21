package steparrik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import steparrik.dto.message.MessageDTO;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.chat.Chat;
import steparrik.model.message.Message;
import steparrik.repository.MessageRepository;
import steparrik.service.kafka.ProducerService;
import steparrik.utils.mapper.message.MessageMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;

    private final MessageMapper messageMapper;

    private final ProducerService producerService;

    public void save(Message message){
        messageRepository.save(message);
    }

    public MessageDTO sendMessage(long id, MessageDTO messageDTO, ProfileUserDto sender, Chat chat) {
        Message message = new Message();

        message.setMessageText(messageDTO.getMessageText());
        message.setSenderId(sender.getId());
        message.setChat(chat);
        message.setTimestamp(LocalDateTime.now());

        save(message);

        MessageDTO messageDto = messageMapper.toDto(message);
        messageDto.setChatType(chat.getChatType());
        messageDto.setChatId(chat.getId());
        producerService.sendMessage(messageDto);
        return messageDto;
    }
}
