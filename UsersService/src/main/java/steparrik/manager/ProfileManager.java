package steparrik.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.user.User;
import steparrik.service.UserService;
import steparrik.utils.mapper.user.ProfileUserMapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfileManager {
    private final UserService userService;

    public ResponseEntity<?> myProfile(String username) {
        if(username == null || username.isEmpty()){
            return ResponseEntity.status(401).body(null);
        }
        Optional<User> user = userService.findUserByUsername(username);
        if(user.isEmpty()){
            return ResponseEntity.status(401).body(null);
        }else {
            ProfileUserMapper profileUserMapper = new ProfileUserMapper();
            ProfileUserDto profileUserDto = profileUserMapper.toDto(user.get());
            return ResponseEntity.ok().body(profileUserDto);
        }
    }
}
