package steparrik.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import steparrik.model.message.Message;
import steparrik.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    @Transactional
    public void save(Message message){
        messageRepository.save(message);
    }
}
