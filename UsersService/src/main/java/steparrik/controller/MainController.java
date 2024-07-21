package steparrik.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;
import steparrik.manager.ProfileManager;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final ProfileManager profileManager;
   // private final EditManager editManager;

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestHeader(value = "X-Username", required = false) String username) {
        return profileManager.myProfile(username);
    }

//    @PutMapping("/profile")
//    public ResponseEntity<?> editProfile(@RequestBody EditUserDto editUserDto, Principal principal) {
//        return editManager.editUser(editUserDto, principal);
//    }


}