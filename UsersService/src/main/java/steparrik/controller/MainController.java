package steparrik.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.user.User;
import steparrik.service.UserService;
import steparrik.utils.jwt.JWTVerification;
import steparrik.utils.mapper.user.ProfileUserMapper;

@RestController
@RequiredArgsConstructor
@Tag(name = "users")
public class MainController {
    private final UserService userService;
    private final ProfileUserMapper profileUserMapper;
    private final JWTVerification jwtVerification;

    @GetMapping("/profile")
    public ProfileUserDto profile(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authData,
                                     @RequestParam(required = false)String username,
                                     @RequestParam(required = false) String phoneNumber) {
        User user;
        if(username == null && phoneNumber == null) {
            user = userService.findUserByUsername(jwtVerification.getUsernameFromJwt(authData));
        }else{
            user = userService.findByUsernameOrPhoneNumber(username, phoneNumber);
        }
        return profileUserMapper.toDto(user);
    }


    @PostMapping("/addAvatar")
    public String photo(@RequestParam("file") MultipartFile file,
                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authData) {
         return userService.uploadAvatar(jwtVerification.getUsernameFromJwt(authData), file);
    }

    @GetMapping("/profile/{id}")
    public ProfileUserDto profile(@PathVariable Long id) {
        User user =  userService.findById(id);
        return profileUserMapper.toDto(user);
    }



}
