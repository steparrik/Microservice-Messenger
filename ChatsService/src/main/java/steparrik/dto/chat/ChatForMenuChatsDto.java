package steparrik.dto.chat;

import lombok.*;
import steparrik.model.chat.ChatType;

import java.io.Serializable;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class ChatForMenuChatsDto implements Serializable {
    private Long id;

    private String name;

    private ChatType chatType;
}
