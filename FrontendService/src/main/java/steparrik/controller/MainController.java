package steparrik.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/auth")
    public String auth(){
        return "auth";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/chats")
    public String chats(){
        return "chats";
    }

    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }

    @GetMapping("/newChat")
    public String newChat(){
        return "newChat";
    }

    @GetMapping("/chat")
    public String chat(){
        return "chat";
    }

    @GetMapping("/addNewParticipant")
    public String addNewParticipant(){
        return "addNewParticipant";
    }




}
