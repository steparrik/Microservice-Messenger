package steparrik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import steparrik.model.user.User;
import steparrik.repository.UserRepository;
import steparrik.utils.exception.ApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(()->
                new ApiException("Нет пользователя с данным id", HttpStatus.NOT_FOUND));
    }

    public User findByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(()->
                new ApiException("Нет пользователя с данным номером телефона", HttpStatus.NOT_FOUND));
    }
    public void save(User user){
        userRepository.save(user);
    }

    public User searchByUsernameOrPhoneNumber(String username, String phoneNumber){
        return userRepository.searchByUsernameOrPhoneNumber(username, phoneNumber).orElseThrow(()->
                new ApiException("Нет пользователя с данным ником или номером телефона", HttpStatus.NOT_FOUND));
    }



}
