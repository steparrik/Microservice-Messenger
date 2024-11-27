package steparrik.dto.message;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.chat.ChatType;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String id;

    @NotEmpty(message = "Текст сообщения не может быть пустым")
    private String messageText;

    private LocalDateTime timestamp;

    private ProfileUserDto sender;

    private ChatType chatType;

    private long chatId;
}
