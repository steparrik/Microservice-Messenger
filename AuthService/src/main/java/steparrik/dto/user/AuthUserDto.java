package steparrik.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthUserDto {
    private String username;
    private String password;
}
