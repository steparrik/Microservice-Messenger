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

import java.util.List;


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
    public List<ChatForMenuChatsDto> chats(@RequestHeader(value = "X-Username") String username) throws InterruptedException {
        return chatService.getChats(username);
    }

    @PostMapping
    public ChatForMenuChatsDto createChat(@RequestHeader(value = "X-Username") String usernameOwner,
                                        @RequestParam(required = false) String username, @RequestParam(required = false) String phoneNumber,
                                        @Valid @RequestBody ChatForMenuChatsDto chatForMenuChatsDto){
        ProfileUserDto owner = userClient.getUserByUsername(usernameOwner);
        Chat chat = chatService.createChat(owner, username, phoneNumber, chatForMenuChatsDto);
        ChatForMenuChatsDto chatForResponse = chatForMenuChatsMapper.toDto(chat);
        return chatService.chooseDialogName(owner, chatForResponse, chat);
    }

    @GetMapping("/{id}")
    public ChatForMenuChatsDto definiteChat(@PathVariable long id, @RequestHeader(value = "X-Username") String username){
        ProfileUserDto profileUserDto = userClient.getUserByUsername(username);
        Chat chat = chatService.getDefiniteChat(id, profileUserDto);
        ChatForCorrespondDto chatForCorrespondDto = chatForCorrespondMapper.toDto(chat);
        return chatService.chooseDialogName(profileUserDto, chatForCorrespondDto, chat);
    }

    @GetMapping("/{id}/participants")
    public List<ProfileUserDto> allParticipants(@PathVariable long id, @RequestHeader(value = "X-Username") String username){
        return chatService.getParticipants(id);
    }

    @PostMapping("/{id}")
    public MessageDTO sendMessage(@PathVariable long id, @Valid @RequestBody MessageDTO messageDTO, @RequestHeader(value = "X-Username") String username){
        ProfileUserDto sender = userClient.getUserByUsername(username);
        Chat chat = chatService.getDefiniteChat(id, sender);

        return messageService.sendMessage(id, messageDTO, sender, chat);
    }

    @PutMapping("/{id}")
    public void addNewParticipant(@PathVariable long id,@RequestParam(required = false) String username,
                                               @RequestParam(required = false) String phoneNumber,
                                               @RequestHeader(value = "X-Username") String usernameOwner){
        ProfileUserDto principalUser = userClient.getUserByUsername(usernameOwner);
        chatService.addParticipant(id, username, phoneNumber, principalUser);
    }


}
