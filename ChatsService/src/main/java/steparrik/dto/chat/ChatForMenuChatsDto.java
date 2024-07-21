package steparrik.dto.chat;

import lombok.*;
import steparrik.model.chat.ChatType;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class ChatForMenuChatsDto {
    private Long id;

    private String name;

    private ChatType chatType;
}
