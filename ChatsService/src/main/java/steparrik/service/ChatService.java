package steparrik.service;

import jakarta.persistence.metamodel.ListAttribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import steparrik.dto.chat.ChatForMenuChatsDto;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.chat.Chat;
import steparrik.model.chat.ChatType;
import steparrik.model.user.User;
import steparrik.repository.ChatRepository;
import steparrik.utils.mapper.chat.ChatForMenuChatsMapper;
import steparrik.utils.mapper.user.ProfileUserMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatForMenuChatsMapper chatForMenuChatsMapper;
    private final ProfileUserMapper profileUserMapper;

    @Transactional
    public void save(Chat chat) {
        chatRepository.save(chat);
    }

    public Optional<Chat> findChatById(Long id) {
        return chatRepository.findById(id);
    }
    public List<ProfileUserDto> getParticipants(long id){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return findChatById(id).get().getParticipants().stream().map(profileUserMapper::toDto).collect(Collectors.toList());
    }


    public List<Chat> chats(User user) {
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
        return chats;
    }

    @Transactional
    public Chat createChat(ChatForMenuChatsDto chatForMenuChatsDto, List<User> participants) {
        Chat chat = chatForMenuChatsMapper.toEntity(chatForMenuChatsDto);
        chat.setParticipants(participants);
        chatRepository.save(chat);
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


}
