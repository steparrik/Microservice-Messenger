package steparrik.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.ServerRequest;
import steparrik.manager.AddAvatarManager;
import steparrik.manager.ProfileManager;
import steparrik.service.firebase.FirebaseStorageService;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final ProfileManager profileManager;
    private final AddAvatarManager addAvatarManager;

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestHeader(value = "X-Username", required = false) String username) {
        return profileManager.myProfile(username);
    }

    @PostMapping("/addAvatar")
    public ResponseEntity<?> photo(@RequestParam("file") MultipartFile file, @RequestHeader(value = "X-Username", required = false) String username) {
         return addAvatarManager.uploadAvatar(username, file);
    }



}
