package steparrik.utils.mapper.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.user.User;
import steparrik.service.UserService;

@Component
@RequiredArgsConstructor
public class ProfileUserMapper  {
    private final UserService userService;

    public ProfileUserDto toDto(User e) {
        if ( e == null ) {
            return null;
        }

        ProfileUserDto profileUserDto = new ProfileUserDto();

        profileUserDto.setUsername( e.getUsername() );
        profileUserDto.setFullName( e.getFullName() );
        profileUserDto.setPhoneNumber( e.getPhoneNumber() );

        return profileUserDto;
    }

}
