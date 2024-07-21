package steparrik.dto.message;


import lombok.*;
import steparrik.dto.user.ProfileUserDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO  {
    private Long id;

    private String messageText;

    private LocalDateTime
            timestamp;

    private ProfileUserDto sender;

    private ChatType chatType;

    private long chatId;
}

enum ChatType {
    DIALOG, GROUP
}
