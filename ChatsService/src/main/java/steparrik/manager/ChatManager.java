package steparrik.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import steparrik.dto.chat.ChatForMenuChatsDto;
import steparrik.dto.message.MessageDTO;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.chat.Chat;
import steparrik.model.user.User;
import steparrik.service.ChatService;
import steparrik.service.UserService;
import steparrik.utils.mapper.chat.ChatForMenuChatsMapper;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatManager {
    private final UserService userService;
    private final ChatService chatService;
    private final ChatForMenuChatsMapper chatForMenuChatsMapper;

    @Cacheable(value = "chatsCache", key = "#username")
    public List<ChatForMenuChatsDto> getChats(String username){
        Optional<User> user = userService.findUserByUsername(username);

        List<Chat> chats = chatService.chats(user.get());

        List<ChatForMenuChatsDto> listChatForMenuChatsDto =  chats.stream().map(chat -> {
            ChatForMenuChatsDto chatForMenuChatsDto = chatForMenuChatsMapper.toDto(chat);
            chatService.chooseDialogName(user.get(), chatForMenuChatsDto, chat);
            return chatForMenuChatsDto;
        }).collect(Collectors.toList());


        return listChatForMenuChatsDto;
    }

    @Cacheable(value = "participantsCache", key = "#id")
    public List<ProfileUserDto> getParticipants(long id) {
        return chatService.getParticipants(id);
    }

}
