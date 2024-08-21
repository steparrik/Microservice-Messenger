package steparrik.utils.mapper.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import steparrik.client.UserClient;
import steparrik.dto.chat.ChatForCorrespondDto;
import steparrik.model.chat.Chat;
import steparrik.utils.mapper.message.MessageMapper;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatForCorrespondMapper {
    private final MessageMapper messageMapper;
    private final UserClient userClient;


    public ChatForCorrespondDto toDto(Chat e) {
        if ( e == null ) {
            return null;
        }

        ChatForCorrespondDto chatForCorrespondDto = new ChatForCorrespondDto();

        chatForCorrespondDto.setId( e.getId() );
        chatForCorrespondDto.setName( e.getName() );
        chatForCorrespondDto.setParticipants(e.getParticipantsId().stream().map(userClient::getUserById).collect(Collectors.toList()));
        chatForCorrespondDto.setChatType( e.getChatType() );
        chatForCorrespondDto.setMessages(e.getMessages().stream().map(messageMapper::toDto).collect(Collectors.toList()));

        return chatForCorrespondDto;
    }

}
