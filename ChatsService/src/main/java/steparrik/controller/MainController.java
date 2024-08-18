package steparrik.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import steparrik.dto.chat.ChatForCorrespondDto;
import steparrik.dto.chat.ChatForMenuChatsDto;
import steparrik.dto.message.MessageDTO;
import steparrik.model.chat.Chat;
import steparrik.model.user.User;
import steparrik.service.ChatService;
import steparrik.service.MessageService;
import steparrik.service.UserService;
import steparrik.utils.mapper.chat.ChatForCorrespondMapper;
import steparrik.utils.mapper.chat.ChatForMenuChatsMapper;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class MainController {
    private final ChatForMenuChatsMapper chatForMenuChatsMapper;
    private final ChatService chatService;
    private final UserService userService;
    private final ChatForCorrespondMapper chatForCorrespondMapper;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<?> chats(@RequestHeader(value = "X-Username") String username) throws InterruptedException {
        User user = userService.findUserByUsername(username);
        return ResponseEntity.ok(chatService.chats(user));
    }

    @PostMapping
    public ResponseEntity<?> createChat(@RequestHeader(value = "X-Username") String usernameOwner,
                                        @RequestParam(required = false) String username, @RequestParam(required = false) String phoneNumber,
                                        @Valid @RequestBody ChatForMenuChatsDto chatForMenuChatsDto){
        User owner = userService.findUserByUsername(usernameOwner);
        Chat chat = chatService.createChat(owner, username, phoneNumber, chatForMenuChatsDto);
        ChatForMenuChatsDto chatForResponse = chatForMenuChatsMapper.toDto(chat);
        return ResponseEntity.ok().body(chatService.chooseDialogName(owner, chatForResponse, chat));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> definiteChat(@PathVariable long id, @RequestHeader(value = "X-Username") String username){
        User user = userService.findUserByUsername(username);
        Chat chat = chatService.getDefiniteChat(id, user);
        ChatForCorrespondDto chatForCorrespondDto = chatForCorrespondMapper.toDto(chat);
        return ResponseEntity.ok().body(chatService.chooseDialogName(user, chatForCorrespondDto, chat));
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<?> allParticipants(@PathVariable long id, @RequestHeader(value = "X-Username") String username){
        return ResponseEntity.ok(chatService.getParticipants(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> sendMessage(@PathVariable long id, @Valid @RequestBody MessageDTO messageDTO, @RequestHeader(value = "X-Username") String username){
        User sender = userService.findUserByUsername(username);
        Chat chat = chatService.getDefiniteChat(id, sender);

        return ResponseEntity.ok().body(messageService.sendMessage(id, messageDTO, sender, chat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> addNewParticipant(@PathVariable long id,@RequestParam(required = false) String username,
                                               @RequestParam(required = false) String phoneNumber,
                                               @RequestHeader(value = "X-Username") String usernameOwner){
        User pricipalUser = userService.findUserByUsername(usernameOwner);
        chatService.addParticipant(id, username, phoneNumber, pricipalUser);
        return ResponseEntity.ok().body(null);
    }


}
