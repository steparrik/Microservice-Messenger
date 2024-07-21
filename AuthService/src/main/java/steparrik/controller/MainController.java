package steparrik.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import steparrik.dto.user.AuthUserDto;
import steparrik.dto.user.RegistrationUserDto;
import steparrik.manager.AuthManager;
import steparrik.manager.CheckJwtManager;
import steparrik.manager.RegistrationManager;


@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final AuthManager authManager;
    private final RegistrationManager registrationManager;
    private final CheckJwtManager checkJwtManager;


    @PostMapping("/auth")
    public ResponseEntity<?> authentication(@RequestBody AuthUserDto authRequest) {
        return authManager.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationUserDto registrationUserDto) {
        return registrationManager.registration(registrationUserDto);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkJwt(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return checkJwtManager.checkJwt(authHeader);
    }


}
