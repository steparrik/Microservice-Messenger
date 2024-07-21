package steparrik.dto.user;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileUserDto  {
    private String username;

    private String phoneNumber;

    private String fullName;
}
