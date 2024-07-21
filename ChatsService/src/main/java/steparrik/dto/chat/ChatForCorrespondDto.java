package steparrik.dto.chat;

import lombok.*;
import steparrik.dto.message.MessageDTO;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.chat.ChatType;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatForCorrespondDto extends ChatForMenuChatsDto {
    private Long id;

    private String name;

    private List<ProfileUserDto> participants;

    private ChatType chatType;

    private List<MessageDTO> messages;
}
