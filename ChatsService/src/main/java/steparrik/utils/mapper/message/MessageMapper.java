package steparrik.utils.mapper.message;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import steparrik.dto.message.MessageDTO;
import steparrik.model.message.Message;

import steparrik.utils.mapper.user.ProfileUserMapper;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final ProfileUserMapper profileUserMapper;

    public MessageDTO toDto(Message e) {
        if ( e == null ) {
            return null;
        }

        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setId( e.getId() );
        messageDTO.setMessageText( e.getMessageText() );
        messageDTO.setTimestamp( e.getTimestamp() );
        messageDTO.setSender(profileUserMapper.toDto(e.getSender()));
        messageDTO.setChatType(e.getChat().getChatType());
        messageDTO.setChatId(e.getChat().getId());

        return messageDTO;
    }
}