package steparrik.utils.mapper.message;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import steparrik.client.UserClient;
import steparrik.dto.message.MessageDTO;
import steparrik.model.chat.Chat;
import steparrik.model.chat.ChatType;
import steparrik.model.message.Message;
import steparrik.service.ChatService;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final UserClient userClient;

    public MessageDTO toDto(Message e, ChatType chatType) {
        if ( e == null ) {
            return null;
        }

        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setId( e.getId() );
        messageDTO.setMessageText( e.getMessageText() );
        messageDTO.setTimestamp( e.getTimestamp() );
        messageDTO.setSender(userClient.getUserById(e.getSenderId()));
        messageDTO.setChatType(chatType);
        messageDTO.setChatId(e.getChatId());

        return messageDTO;
    }

}
