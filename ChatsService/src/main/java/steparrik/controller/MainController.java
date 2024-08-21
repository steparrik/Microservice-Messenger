package steparrik.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import steparrik.client.UserClient;
import steparrik.dto.chat.ChatForCorrespondDto;
import steparrik.dto.chat.ChatForMenuChatsDto;
import steparrik.dto.message.MessageDTO;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.chat.Chat;
import steparrik.service.ChatService;
import steparrik.service.MessageService;
import steparrik.utils.mapper.chat.ChatForCorrespondMapper;
import steparrik.utils.mapper.chat.ChatForMenuChatsMapper;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class MainController {
    private final ChatForMenuChatsMapper chatForMenuChatsMapper;
    private final ChatService chatService;
    private final ChatForCorrespondMapper chatForCorrespondMapper;
    private final MessageService messageService;

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<?> chats(@RequestHeader(value = "X-Username") String username) throws InterruptedException {
        return ResponseEntity.ok(chatService.getChats(username));
    }

    @PostMapping
    public ResponseEntity<?> createChat(@RequestHeader(value = "X-Username") String usernameOwner,
                                        @RequestParam(required = false) String username, @RequestParam(required = false) String phoneNumber,
                                        @Valid @RequestBody ChatForMenuChatsDto chatForMenuChatsDto){
        ProfileUserDto owner = userClient.getUserByUsername(usernameOwner);
        Chat chat = chatService.createChat(owner, username, phoneNumber, chatForMenuChatsDto);
        ChatForMenuChatsDto chatForResponse = chatForMenuChatsMapper.toDto(chat);
        return ResponseEntity.ok().body(chatService.chooseDialogName(owner, chatForResponse, chat));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> definiteChat(@PathVariable long id, @RequestHeader(value = "X-Username") String username){
        ProfileUserDto profileUserDto = userClient.getUserByUsername(username);
        Chat chat = chatService.getDefiniteChat(id, profileUserDto);
        ChatForCorrespondDto chatForCorrespondDto = chatForCorrespondMapper.toDto(chat);
        return ResponseEntity.ok().body(chatService.chooseDialogName(profileUserDto, chatForCorrespondDto, chat));
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<?> allParticipants(@PathVariable long id, @RequestHeader(value = "X-Username") String username){
        return ResponseEntity.ok(chatService.getParticipants(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> sendMessage(@PathVariable long id, @Valid @RequestBody MessageDTO messageDTO, @RequestHeader(value = "X-Username") String username){
        ProfileUserDto sender = userClient.getUserByUsername(username);
        Chat chat = chatService.getDefiniteChat(id, sender);

        return ResponseEntity.ok().body(messageService.sendMessage(id, messageDTO, sender, chat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> addNewParticipant(@PathVariable long id,@RequestParam(required = false) String username,
                                               @RequestParam(required = false) String phoneNumber,
                                               @RequestHeader(value = "X-Username") String usernameOwner){
        ProfileUserDto principalUser = userClient.getUserByUsername(usernameOwner);
        chatService.addParticipant(id, username, phoneNumber, principalUser);
        return ResponseEntity.ok().body(null);
    }


}
