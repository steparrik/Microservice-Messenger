package steparrik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import steparrik.dto.chat.ChatForMenuChatsDto;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.chat.Chat;
import steparrik.model.chat.ChatType;
import steparrik.model.message.Message;
import steparrik.model.user.User;
import steparrik.repository.ChatRepository;
import steparrik.utils.exception.ApiException;
import steparrik.utils.mapper.chat.ChatForMenuChatsMapper;
import steparrik.utils.mapper.user.ProfileUserMapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final ChatForMenuChatsMapper chatForMenuChatsMapper;
    private final ProfileUserMapper profileUserMapper;


    public void save(Chat chat) {
        chatRepository.save(chat);
    }

    public Chat findChatById(Long id) {
        return chatRepository.findById(id).orElseThrow(() ->
                new ApiException("Нет чата с данным id", HttpStatus.NOT_FOUND));
    }
    @Cacheable(value = "participantsCache", key = "#id")
    public List<ProfileUserDto> getParticipants(long id){
        return findChatById(id).getParticipants().stream().map(profileUserMapper::toDto).collect(Collectors.toList());
    }

    @Cacheable(value = "chats", key = "#user.username")
    public List<ChatForMenuChatsDto> chats(User user) {
        List<Chat> chats =  chatRepository.findAllByParticipants(user);

        chats.sort(new Comparator<Chat>() {
            @Override
            public int compare(Chat o1, Chat o2) {
                if (o1.getMessages() == null || o1.getMessages().isEmpty()) {
                    return (o2.getMessages() == null || o2.getMessages().isEmpty()) ? 0 : 1;
                }

                if (o2.getMessages() == null || o2.getMessages().isEmpty()) {
                    return -1;
                }

                return o2.getMessages().get(o2.getMessages().size()-1).getTimestamp().compareTo(o1.getMessages().get(o1.getMessages().size()-1).getTimestamp());
            }
        });

        List<ChatForMenuChatsDto> listChatForMenuChatsDto =  chats.stream().map(chat -> {
            ChatForMenuChatsDto chatForMenuChatsDto = chatForMenuChatsMapper.toDto(chat);
            chooseDialogName(user, chatForMenuChatsDto, chat);
            return chatForMenuChatsDto;
        }).collect(Collectors.toList());

        return listChatForMenuChatsDto;
    }


    public Chat createChat(User owner, String username, String phoneNumber, ChatForMenuChatsDto chatForMenuChatsDto) {
        if (chatForMenuChatsDto.getChatType().equals(ChatType.DIALOG)) {
            return createDialog(owner, username, phoneNumber);
        } else {
            return createGroupChat(owner, chatForMenuChatsDto);
        }
    }


    public Chat createGroupChat(User owner,  ChatForMenuChatsDto chatForMenuChatsDto){
        Chat chat = new Chat();

        if(chatForMenuChatsDto.getName().isEmpty()) {
            throw new ApiException("Имя группы должно быть задано обязательно", HttpStatus.BAD_REQUEST);
        }
        chat.setChatType(ChatType.GROUP);
        chat.setParticipants(Collections.singletonList(owner));
        chat.setName(chatForMenuChatsDto.getName());
        save(chat);
        return chat;
    }

    public Chat createDialog(User owner,
                           String username, String phoneNumber){
        Chat chat = new Chat();

        User userForAdd = userService.searchByUsernameOrPhoneNumber(username, phoneNumber);
        if(userForAdd.getUsername().equals(owner.getUsername())){
            throw new ApiException("Вы не можете создать чат с собой", HttpStatus.BAD_REQUEST);
        }
        if(findYetAddedDialogs(owner, userForAdd)){
            throw new ApiException("Чат с этим пользователем уже существует", HttpStatus.BAD_REQUEST);
        }
        chat.setChatType(ChatType.DIALOG);
        chat.setParticipants(List.of(owner, userForAdd));
        save(chat);
        return chat;

    }

    public ChatForMenuChatsDto chooseDialogName(User user, ChatForMenuChatsDto chatForMenuChatsDto, Chat chat) {
        if (chatForMenuChatsDto.getChatType().equals(ChatType.DIALOG)) {
            if (chat.getParticipants().get(0).getUsername().equals(user.getUsername())) {
                chatForMenuChatsDto.setName(chat.getParticipants().get(1).getFullName());
            } else {
                chatForMenuChatsDto.setName(chat.getParticipants().get(0).getFullName());
            }
        }
        return chatForMenuChatsDto;
    }

    public boolean findYetAddedDialogs(User owner, User participant) {
        for(Chat chat : owner.getChats()){
            if(chat.getParticipants().contains(participant) && chat.getChatType().equals(ChatType.DIALOG)){
                return  true;
            }
        }
        return false;
    }


    public Chat getDefiniteChat(long id, User user){
        Chat chat = findChatById(id);


        if(!chat.getParticipants().contains(user)){
            throw new ApiException("В вашем списке чатов, нет чата с данным ID", HttpStatus.NOT_FOUND);
        }

        chat.getMessages().sort(new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });

        return chat;
    }

    public void addParticipant(long id, String username, String phoneNumber, User principalUser) {
        Chat chat = findChatById(id);

        if(chat.getChatType().equals(ChatType.DIALOG)){
            throw new ApiException("Добавлять людей в DIALOG нельзя", HttpStatus.BAD_REQUEST);
        }

        if(!chat.getParticipants().contains(principalUser)){
            throw new ApiException("Чат с данным id не найден в списке ваших чатов", HttpStatus.NOT_FOUND);
        }
        User userForAdd = userService.searchByUsernameOrPhoneNumber(username, phoneNumber);
        if(chat.getParticipants().contains(userForAdd)){
            throw new ApiException("Пользователь " + userForAdd.getUsername()+ " уже добавлен", HttpStatus.BAD_REQUEST);
        }
        chat.getParticipants().add(userForAdd);
        save(chat);
    }
}
