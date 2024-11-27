package steparrik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import steparrik.dto.analytics.VisitTimeDto;
import steparrik.dto.message.MessageDTO;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.chat.Chat;
import steparrik.model.message.Message;
import steparrik.repository.ChatRepository;
import steparrik.repository.MessageRepository;
import steparrik.service.kafka.ProducerService;
import steparrik.utils.exception.ApiException;
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

    public Message findById(String id){
        return messageRepository.findById(id).orElseThrow(() ->
                new ApiException("Нет сообщения с данным id", HttpStatus.NOT_FOUND));
    }

    public MessageDTO sendMessage(long id, MessageDTO messageDTO, ProfileUserDto sender, Chat chat) {
        Message message = new Message();

        message.setMessageText(messageDTO.getMessageText());
        message.setSenderId(sender.getId());
        message.setChatId(chat.getId());
        message.setTimestamp(LocalDateTime.now());

        save(message);
        Chat existingChat = chatService.findChatById(chat.getId());
        existingChat.getMessagesId().add(message.getId());

        chatService.save(existingChat);
        MessageDTO messageDto = messageMapper.toDto(message, chat.getChatType());
        messageDto.setChatType(chat.getChatType());
        messageDto.setChatId(chat.getId());
        producerService.sendMessage(messageDto);
        VisitTimeDto visitTimeDto = new VisitTimeDto(sender.getId(), LocalDateTime.now());
        producerService.sendTimeToAnalyticsService(visitTimeDto);
        return messageDto;
    }
}
