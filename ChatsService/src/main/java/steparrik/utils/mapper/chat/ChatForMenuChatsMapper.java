package steparrik.utils.mapper.chat;

import org.springframework.stereotype.Component;
import steparrik.dto.chat.ChatForMenuChatsDto;
import steparrik.model.chat.Chat;

@Component
public class ChatForMenuChatsMapper {
    public Chat toEntity(ChatForMenuChatsDto d) {
        if ( d == null ) {
            return null;
        }

        Chat chat = new Chat();

        chat.setId( d.getId() );
        chat.setName( d.getName() );
        chat.setChatType( d.getChatType() );

        return chat;
    }

    public ChatForMenuChatsDto toDto(Chat e) {
        if ( e == null ) {
            return null;
        }

        ChatForMenuChatsDto chatForMenuChatsDto = new ChatForMenuChatsDto();

        chatForMenuChatsDto.setId( e.getId() );
        chatForMenuChatsDto.setName( e.getName() );
        chatForMenuChatsDto.setChatType( e.getChatType() );

        return chatForMenuChatsDto;
    }
}
