package steparrik.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import steparrik.dto.user.AuthUserDto;
import steparrik.dto.user.EditUserDto;
import steparrik.dto.user.RegistrationUserDto;
import steparrik.service.AuthService;
import steparrik.service.RegistrationService;
import steparrik.service.UserService;
import steparrik.utils.validate.token.JwtValidate;


@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final AuthService authService;
    private final RegistrationService registrationService;
    private final JwtValidate jwtValidate;
    private final UserService userService;


    @PostMapping("/auth")
    public ResponseEntity<?> authentication(@Valid @RequestBody AuthUserDto authRequest) {
        return ResponseEntity.ok().body(authService.createAuthToken(authRequest));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        registrationService.registration(registrationUserDto);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkJwt(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return ResponseEntity.ok().body(jwtValidate.checkJwt(authHeader));
    }


    @PutMapping("/editUser")
    public ResponseEntity<?> editProfile(@Valid@RequestBody EditUserDto editUserDto, @RequestHeader(value = "X-Username", required = true) String username) {
        return ResponseEntity.ok().body(userService.editUser(editUserDto, username));
    }



}
