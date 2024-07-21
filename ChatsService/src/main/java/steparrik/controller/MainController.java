package steparrik.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import steparrik.dto.chat.ChatForMenuChatsDto;
import steparrik.dto.message.MessageDTO;
import steparrik.manager.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class MainController {
    private final CreateChatManager createChatManager;
    private final ChatManager chatManager;
    private final DefiniteChatManager definiteChatManager;
    private final SendMessageManager sendMessageManager;
    private final AddParticipantManager addParticipantManager;

    @GetMapping
    public ResponseEntity<?> chats(@RequestHeader(value = "X-Username", required = false) String username) {
        return chatManager.getChats(username);
    }

    @PostMapping
    public ResponseEntity<?> createChat(@RequestHeader(value = "X-Username", required = false) String usernameOwner,
                                        @RequestParam(required = false) String username, @RequestParam(required = false) String phoneNumber,
                                        @RequestBody(required = false) ChatForMenuChatsDto chatForMenuChatsDto){
        return createChatManager.createChat(usernameOwner, chatForMenuChatsDto, username, phoneNumber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> definiteChat(@PathVariable long id, @RequestHeader(value = "X-Username", required = false) String username){
        return definiteChatManager.getDefiniteChat(id, username);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> sendMessage(@PathVariable long id, @RequestBody MessageDTO messageDTO, @RequestHeader(value = "X-Username", required = false) String username){
        return sendMessageManager.sendMessage(id, messageDTO, username);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> addNewParticipant(@PathVariable long id, @RequestParam(required = false) String username, @RequestHeader(value = "X-Username", required = false) String usernameOwner){
        return  addParticipantManager.addParticipant(id, username, usernameOwner);
    }


}
