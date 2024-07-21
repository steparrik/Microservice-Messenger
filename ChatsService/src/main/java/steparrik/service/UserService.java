package steparrik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import steparrik.model.user.User;
import steparrik.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public Optional<User> findByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber);
    }
    @Transactional
    public void save(User user){
        userRepository.save(user);
    }

    public Optional<User> searchByUsernameOrPhoneNumber(String username, String phoneNumber){
        Optional<User> user = userRepository.searchByUsernameOrPhoneNumber(username, phoneNumber);
        return user;
    }



}
