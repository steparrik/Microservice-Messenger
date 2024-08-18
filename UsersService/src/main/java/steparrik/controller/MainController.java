package steparrik.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import steparrik.model.user.User;
import steparrik.service.UserService;
import steparrik.utils.mapper.user.ProfileUserMapper;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final ProfileUserMapper profileUserMapper;

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestHeader(value = "X-Username") String username) {
        User user =  userService.findUserByUsername(username);
        return ResponseEntity.ok().body(profileUserMapper.toDto(user));
    }


    @PostMapping("/addAvatar")
    public ResponseEntity<?> photo(@RequestParam("file") MultipartFile file,
                                   @RequestHeader(value = "X-Username") String username) {
         return ResponseEntity.ok().body(userService.uploadAvatar(username, file));
    }



}
