package steparrik.dto.user;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileUserDto implements Serializable {
    private String username;

    private String phoneNumber;

    private String fullName;
}
