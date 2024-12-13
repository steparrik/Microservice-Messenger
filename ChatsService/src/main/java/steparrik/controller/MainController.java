package steparrik.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import steparrik.client.UserClient;
import steparrik.dto.chat.ChatForCorrespondDto;
import steparrik.dto.chat.ChatForMenuChatsDto;
import steparrik.dto.message.MessageDTO;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.chat.Chat;
import steparrik.service.ChatService;
import steparrik.service.MessageService;
import steparrik.utils.jwt.JWTVerification;
import steparrik.utils.mapper.chat.ChatForCorrespondMapper;
import steparrik.utils.mapper.chat.ChatForMenuChatsMapper;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
@Tag(name = "chats")
@Slf4j
public class MainController {
    private final ChatForMenuChatsMapper chatForMenuChatsMapper;
    private final ChatService chatService;
    private final ChatForCorrespondMapper chatForCorrespondMapper;
    private final MessageService messageService;
    private final UserClient userClient;
//    private final JWTVerification jwtVerification;

    @GetMapping
    public List<ChatForMenuChatsDto> chats(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader)  {
        log.info("Бро получил чаты");
        return chatService.getChats(authHeader);
    }

    @PostMapping
    public ChatForMenuChatsDto createChat(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                        @RequestParam(required = false) String username, @RequestParam(required = false) String phoneNumber,
                                        @Valid @RequestBody ChatForMenuChatsDto chatForMenuChatsDto){
        Chat chat = chatService.createChat(authHeader, username, phoneNumber, chatForMenuChatsDto);
        ChatForMenuChatsDto chatForResponse = chatForMenuChatsMapper.toDto(chat);
        log.info("Бро создал чат");
        return chatService.chooseDialogName(authHeader, chatForResponse, chat);
    }

    @GetMapping("/{id}")
    public ChatForMenuChatsDto definiteChat(@PathVariable long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        ProfileUserDto profileUserDto = userClient.getUserByUsername(authHeader);
        Chat chat = chatService.getDefiniteChat(id, profileUserDto);
        ChatForCorrespondDto chatForCorrespondDto = chatForCorrespondMapper.toDto(chat);
        return chatService.chooseDialogName(authHeader, chatForCorrespondDto, chat);
    }

    @GetMapping("/{id}/participants")
    public List<ProfileUserDto> allParticipants(@PathVariable long id){
        return chatService.getParticipants(id);
    }

    @PostMapping("/{id}")
    public MessageDTO sendMessage(@PathVariable long id, @Valid @RequestBody MessageDTO messageDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        ProfileUserDto sender = userClient.getUserByUsername(authHeader);
        Chat chat = chatService.getDefiniteChat(id, sender);

        return messageService.sendMessage(id, messageDTO, sender, chat);
    }

    @PutMapping("/{id}")
    public void addNewParticipant(@PathVariable long id,@RequestParam(required = false) String username,
                                               @RequestParam(required = false) String phoneNumber,
                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String authData){
        chatService.addParticipant(id, username, phoneNumber, authData);
    }


}
