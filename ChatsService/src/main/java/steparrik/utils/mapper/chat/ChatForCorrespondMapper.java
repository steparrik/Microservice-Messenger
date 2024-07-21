package steparrik.utils.mapper.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import steparrik.dto.chat.ChatForCorrespondDto;
import steparrik.model.chat.Chat;
import steparrik.service.UserService;
import steparrik.utils.mapper.message.MessageMapper;
import steparrik.utils.mapper.user.ProfileUserMapper;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatForCorrespondMapper {
    private final ProfileUserMapper profileUserMapper;
    private final MessageMapper messageMapper;


    public ChatForCorrespondDto toDto(Chat e) {
        if ( e == null ) {
            return null;
        }

        ChatForCorrespondDto chatForCorrespondDto = new ChatForCorrespondDto();

        chatForCorrespondDto.setId( e.getId() );
        chatForCorrespondDto.setName( e.getName() );
        chatForCorrespondDto.setParticipants( e.getParticipants().stream().map(profileUserMapper::toDto).collect(Collectors.toList()));
        chatForCorrespondDto.setChatType( e.getChatType() );
        chatForCorrespondDto.setMessages(e.getMessages().stream().map(messageMapper::toDto).collect(Collectors.toList()));

        return chatForCorrespondDto;
    }

}
