package steparrik.service;

import com.google.cloud.storage.Blob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import steparrik.model.user.User;
import steparrik.repository.UserRepository;
import steparrik.service.firebase.FirebaseStorageService;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FirebaseStorageService firebaseStorageService;

    public Optional<User> findUserByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Transactional
    public void save(User user){
        userRepository.save(user);
    }

    @Transactional
    public String uploadAvatar(String username, MultipartFile file) throws IOException {
        User user = findUserByUsername(username).get();
        Blob blob = firebaseStorageService.uploadFile(file);
        String link = blob.getMediaLink();
        user.setPathToAvatar(link);
        save(user);
        return link;
    }
}
