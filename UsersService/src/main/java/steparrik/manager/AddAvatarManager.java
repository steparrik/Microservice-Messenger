package steparrik.manager;

import com.google.gson.internal.bind.JsonTreeReader;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import steparrik.service.UserService;
import steparrik.usersservice.utils.exceptions.ExceptionEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AddAvatarManager {
    private final UserService userService;

    public ResponseEntity<?> uploadAvatar(String username, MultipartFile file){
        try {
            return ResponseEntity.ok().body(userService.uploadAvatar(username, file));
        }catch (IOException e){
            return ResponseEntity.status(404).body(new ExceptionEntity(e.getMessage(), LocalDateTime.now()));
        }
    }
}
