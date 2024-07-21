package steparrik.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import steparrik.dto.token.JwtResponseDto;
import steparrik.dto.user.AuthUserDto;
import steparrik.service.AuthService;
import steparrik.utils.exceptions.ExceptionEntity;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthManager {
    private final AuthService authService;

    public ResponseEntity<?> createAuthToken(@RequestBody AuthUserDto authRequest) {
        JwtResponseDto jwtResponseDto = authService.createAuthToken(authRequest);
        if (jwtResponseDto == null) {
            return new ResponseEntity<>(new ExceptionEntity("Неправильный логин или пароль", LocalDateTime.now()), HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(jwtResponseDto);
        }
    }
}
