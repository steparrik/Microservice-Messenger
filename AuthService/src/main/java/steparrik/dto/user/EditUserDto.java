package steparrik.dto.user;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EditUserDto {
    private String username;

    private String fullName;

    private String password;
}
