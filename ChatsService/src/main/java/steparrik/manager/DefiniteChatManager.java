package steparrik.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import steparrik.dto.chat.ChatForCorrespondDto;
import steparrik.dto.message.MessageDTO;
import steparrik.model.chat.Chat;
import steparrik.model.user.User;
import steparrik.service.ChatService;
import steparrik.service.UserService;
import steparrik.utils.exceptions.ExceptionEntity;
import steparrik.utils.mapper.chat.ChatForCorrespondMapper;
import steparrik.utils.mapper.user.ProfileUserMapper;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefiniteChatManager {
    private final UserService userService;
    private final ChatService chatService;
    private final ChatForCorrespondMapper chatForCorrespondMapper;
    private final ProfileUserMapper profileUserMapper;

    public ResponseEntity<?> getDefiniteChat(long id, String username){
        Optional<Chat> chat = chatService.findChatById(id);
        User user = userService.findUserByUsername(username).get();
        if(chat.isPresent()) {
            if(!chat.get().getParticipants().contains(user)){
                return new ResponseEntity<>(new ExceptionEntity("В вашем списке чатов, нет чата с данным ID", LocalDateTime.now()), HttpStatus.NOT_FOUND);
            }
            ChatForCorrespondDto chatForCorrespondDto = chatForCorrespondMapper.toDto(chat.get());

            chatForCorrespondDto.getMessages().sort(new Comparator<MessageDTO>() {
                @Override
                public int compare(MessageDTO o1, MessageDTO o2) {
                    return o1.getTimestamp().compareTo(o2.getTimestamp());
                }
            });

            chatService.chooseDialogName(user, chatForCorrespondDto, chat.get());
            return ResponseEntity.ok(chatForCorrespondDto);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Нет чата с данным ID");
        }
    }
}

