package steparrik.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import steparrik.dto.user.ProfileUserDto;
import steparrik.model.user.User;
import steparrik.service.UserService;
import steparrik.utils.mapper.user.ProfileUserMapper;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final ProfileUserMapper profileUserMapper;

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestHeader(value = "X-Username") String principalUsername,
                                     @RequestParam(required = false)String username,
                                     @RequestParam(required = false) String phoneNumber) {
        User user;
        if(username == null && phoneNumber == null) {
            user = userService.findUserByUsername(principalUsername);
        }else{
            user = userService.findByUsernameOrPhoneNumber(username, phoneNumber);
        }
        return ResponseEntity.ok().body(profileUserMapper.toDto(user));
    }


    @PostMapping("/addAvatar")
    public ResponseEntity<?> photo(@RequestParam("file") MultipartFile file,
                                   @RequestHeader(value = "X-Username") String username) {
         return ResponseEntity.ok().body(userService.uploadAvatar(username, file));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> profile(@PathVariable Long id) {
        User user =  userService.findById(id);
        return ResponseEntity.ok().body(profileUserMapper.toDto(user));
    }



}
