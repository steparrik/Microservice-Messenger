package steparrik.dto.chat;

import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Тип чата должен быть задан обязательно")
    private ChatType chatType;
}
