package steparrik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import steparrik.client.UserClient;
import steparrik.dto.chat.ChatForMenuChatsDto;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.chat.Chat;
import steparrik.model.chat.ChatType;
import steparrik.model.message.Message;
import steparrik.repository.ChatRepository;
import steparrik.utils.exception.ApiException;
import steparrik.utils.jwt.JWTVerification;
import steparrik.utils.mapper.chat.ChatForMenuChatsMapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserClient userClient;
    private final ChatForMenuChatsMapper chatForMenuChatsMapper;
    private final JWTVerification jwtVerification;


    public void save(Chat chat) {
        chatRepository.save(chat);
    }

    public Chat findChatById(Long id) {
        return chatRepository.findById(id).orElseThrow(() ->
                new ApiException("Нет чата с данным id", HttpStatus.NOT_FOUND));
    }
    @Cacheable(value = "participantsCache", key = "#id")
    public List<ProfileUserDto> getParticipants(long id){
        List<Long> participantsId = findChatById(id).getParticipantsId();
        return participantsId.stream().map(userClient::getUserById).collect(Collectors.toList());
    }

//    @Cacheable(value = "chats", key = "#username") due to the fact that I changed the format and now I’m transferring a token, fixed later
    public List<ChatForMenuChatsDto> getChats(String authData) {
        ProfileUserDto profileUserDto = userClient.getUserByUsername(authData);
        List<Chat> chats =  chatRepository.findAllByParticipantsId(profileUserDto.getId());


        chats.sort((o1, o2) -> {
                if (o1.getMessages() == null || o1.getMessages().isEmpty()) {
                    return (o2.getMessages() == null || o2.getMessages().isEmpty()) ? 0 : 1;
                }
                if (o2.getMessages() == null || o2.getMessages().isEmpty()) {
                    return -1;
                }
                return o2.getMessages().get(o2.getMessages().size()-1).getTimestamp().compareTo(o1.getMessages().get(o1.getMessages().size()-1).getTimestamp());
        });


        List<ChatForMenuChatsDto> listChatForMenuChatsDto =  chats.stream().map(chat -> {
            ChatForMenuChatsDto chatForMenuChatsDto = chatForMenuChatsMapper.toDto(chat);
            chooseDialogName(authData, chatForMenuChatsDto, chat);
            return chatForMenuChatsDto;
        }).collect(Collectors.toList());

        return listChatForMenuChatsDto;
    }


    public Chat createChat(String authData, String username, String phoneNumber, ChatForMenuChatsDto chatForMenuChatsDto) {
        if (chatForMenuChatsDto.getChatType().equals(ChatType.DIALOG)) {
            return createDialog(authData, username, phoneNumber);
        } else {
            return createGroupChat(authData, chatForMenuChatsDto);
        }
    }


    public Chat createGroupChat(String authData,  ChatForMenuChatsDto chatForMenuChatsDto){
        Chat chat = new Chat();
        ProfileUserDto owner = userClient.getUserByUsername(jwtVerification.getUsernameFromJwt(authData));
        if(chatForMenuChatsDto.getName() == null || chatForMenuChatsDto.getName().isEmpty()) {
            throw new ApiException("Имя группы должно быть задано обязательно", HttpStatus.BAD_REQUEST);
        }
        chat.setChatType(ChatType.GROUP);
        chat.setParticipantsId(Collections.singletonList(owner.getId()));
        chat.setName(chatForMenuChatsDto.getName());
        save(chat);
        return chat;
    }

    public Chat createDialog(String authData,
                           String username, String phoneNumber){
        Chat chat = new Chat();
        ProfileUserDto owner = userClient.getUserByUsername(authData);
        ProfileUserDto userForAdd = userClient.getUserByUsernameOrPhoneNumber(owner.getUsername(), username, phoneNumber);
        if(userForAdd.getUsername().equals(owner.getUsername())){
            throw new ApiException("Вы не можете создать чат с собой", HttpStatus.BAD_REQUEST);
        }
        if(findYetAddedDialogs(owner, userForAdd)){
            throw new ApiException("Чат с этим пользователем уже существует", HttpStatus.BAD_REQUEST);
        }
        chat.setChatType(ChatType.DIALOG);
        chat.setParticipantsId(List.of(owner.getId(), userForAdd.getId()));
        save(chat);
        return chat;

    }

    public ChatForMenuChatsDto chooseDialogName(String authData, ChatForMenuChatsDto chatForMenuChatsDto, Chat chat) {
        ProfileUserDto profileUserDto = userClient.getUserByUsername(authData);
        if (chatForMenuChatsDto.getChatType().equals(ChatType.DIALOG)) {
            ProfileUserDto participant1 = userClient.getUserById(chat.getParticipantsId().get(0));
            ProfileUserDto participant2 = userClient.getUserById(chat.getParticipantsId().get(1));
            if (participant1.getUsername().equals(profileUserDto.getUsername())) {
                chatForMenuChatsDto.setName(participant2.getFullName());
            } else {
                chatForMenuChatsDto.setName(participant1.getFullName());
            }
        }
        return chatForMenuChatsDto;
    }

    public boolean findYetAddedDialogs(ProfileUserDto owner, ProfileUserDto participant) {
        List<Chat> chats = chatRepository.findAllByParticipantsId(owner.getId());
        for(Chat chat : chats){
            if(chat.getParticipantsId().contains(participant.getId()) && chat.getChatType().equals(ChatType.DIALOG)){
                return  true;
            }
        }
        return false;
    }



    public Chat getDefiniteChat(long id, ProfileUserDto profileUserDto){
        Chat chat = findChatById(id);

        if(!chat.getParticipantsId().contains(profileUserDto.getId())){
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

    public void addParticipant(long id, String username, String phoneNumber, String authData) {
        Chat chat = findChatById(id);
        ProfileUserDto principalUser = userClient.getUserByUsername(authData);
        if(chat.getChatType().equals(ChatType.DIALOG)){
            throw new ApiException("Добавлять людей в DIALOG нельзя", HttpStatus.BAD_REQUEST);
        }

        if(!chat.getParticipantsId().contains(principalUser.getId())){
            throw new ApiException("Чат с данным id не найден в списке ваших чатов", HttpStatus.NOT_FOUND);
        }
        ProfileUserDto userForAdd = userClient.getUserByUsernameOrPhoneNumber(authData, username, phoneNumber);
        if(chat.getParticipantsId().contains(userForAdd.getId())){
            throw new ApiException("Пользователь " + userForAdd.getUsername()+ " уже добавлен", HttpStatus.BAD_REQUEST);
        }
        chat.getParticipantsId().add(userForAdd.getId());
        save(chat);
    }
}
