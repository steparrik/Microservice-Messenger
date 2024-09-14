package steparrik.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import steparrik.dto.token.JwtResponseDto;
import steparrik.dto.user.AuthUserDto;
import steparrik.dto.user.EditUserDto;
import steparrik.dto.user.RegistrationUserDto;
import steparrik.service.AuthService;
import steparrik.service.RegistrationService;
import steparrik.service.UserService;
import steparrik.utils.validate.token.JwtValidate;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final AuthService authService;
    private final RegistrationService registrationService;
    private final JwtValidate jwtValidate;
    private final UserService userService;


    @PostMapping("/auth")
    public JwtResponseDto authentication(@Valid @RequestBody AuthUserDto authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public void registration(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        registrationService.registration(registrationUserDto);
    }

    @GetMapping("/check")
    public Map<String, String> checkJwt(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return jwtValidate.checkJwt(authHeader);
    }


    @PutMapping("/editUser")
    public JwtResponseDto editProfile(@Valid@RequestBody EditUserDto editUserDto, @RequestHeader(value = "X-Username", required = true) String username) {
        return userService.editUser(editUserDto, username);
    }



}
