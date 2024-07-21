package steparrik.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import steparrik.model.chat.Chat;
import steparrik.model.chat.ChatType;
import steparrik.model.user.User;
import steparrik.service.ChatService;
import steparrik.service.UserService;
import steparrik.utils.exceptions.ExceptionEntity;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AddParticipantManager {
    private final UserService userService;
    private final ChatService chatService;

    @Transactional
    public ResponseEntity<?> addParticipant(long id, String username, String usernameOwner) {
        if(username==null || username.isEmpty()){
            return new ResponseEntity<>(new ExceptionEntity("Недостаточно данных", LocalDateTime.now()), HttpStatus.NOT_FOUND);
        }

        Optional<Chat> chat = chatService.findChatById(id);
        User principalUser = userService.findUserByUsername(usernameOwner).get();

        if(chat.isPresent()){
            if(chat.get().getChatType().equals(ChatType.DIALOG)){
                return new ResponseEntity<>(new ExceptionEntity("Добавлять людей в DIALOG нельзя", LocalDateTime.now()), HttpStatus.NOT_FOUND);

            }
            if(!chat.get().getParticipants().contains(principalUser)){
                return new ResponseEntity<>(new ExceptionEntity("Чат с данным id не найден в списке ваших чатов", LocalDateTime.now()), HttpStatus.NOT_FOUND);

            }
            Optional<User> userForAdd = userService.findUserByUsername(username);
            if(userForAdd.isPresent()){
                if(chat.get().getParticipants().contains(userForAdd.get())){
                    return new ResponseEntity<>(new ExceptionEntity("Пользователь " + userForAdd.get().getUsername()+ " уже добавлен", LocalDateTime.now()), HttpStatus.NOT_FOUND);

                }
                chat.get().getParticipants().add(userForAdd.get());
                chatService.save(chat.get());
                return ResponseEntity.ok().body(null);
            }else{
                return new ResponseEntity<>(new ExceptionEntity("Пользователь с таким ником не найден", LocalDateTime.now()), HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>(new ExceptionEntity("Нет чата с таким ID", LocalDateTime.now()), HttpStatus.NOT_FOUND);
        }
    }
}
