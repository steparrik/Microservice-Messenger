package steparrik.dto.user;

import lombok.*;
import org.apache.kafka.common.protocol.types.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileUserDto  {
    private String username;

    private String phoneNumber;

    private String fullName;

    private String pathToAvatar;
}
