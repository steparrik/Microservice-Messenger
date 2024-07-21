package steparrik.dto.message;

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
    private Long id;

    private String messageText;

    private LocalDateTime timestamp;

    private ProfileUserDto sender;

    private ChatType chatType;

    private long chatId;
}