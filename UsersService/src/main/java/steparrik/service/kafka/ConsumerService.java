package steparrik.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import steparrik.dto.user.EditUserKafkaDto;
import steparrik.dto.user.RegistrationUserDto;
import steparrik.model.user.User;
import steparrik.service.UserService;
import steparrik.utils.mapper.user.RegistrationUserMapper;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {
    private final UserService userService;
    private final RegistrationUserMapper registrationUserMapper;

    @Value("${PATH_TO_DEFAULT_AVATAR}")
    private String pathToDefAvatar;

    @KafkaListener(topics = "save-topic", groupId = "A")
    public void saveListener(RegistrationUserDto registrationUserDto) {
        User user = registrationUserMapper.toEntity(registrationUserDto);
        user.setPathToAvatar(pathToDefAvatar);
        userService.save(user);
        log.info(registrationUserDto.getUsername() + " добавлен в users");
    }


    @KafkaListener(topics = "edit-topic", groupId = "A")
    public void editListener(EditUserKafkaDto editUserKafkaDto) {
        Optional<User> user = userService.findUserByUsername(editUserKafkaDto.getOldUsername());
        if(user.isPresent()){
            if(editUserKafkaDto.getPassword()!=null){
                user.get().setPassword(editUserKafkaDto.getPassword());
            }
            if(editUserKafkaDto.getUsername()!=null){
                user.get().setUsername(editUserKafkaDto.getUsername());
            }
            if(editUserKafkaDto.getFullName()!=null){
                user.get().setFullName(editUserKafkaDto.getFullName());
            }

            userService.save(user.get());
        }
        log.info(user.get().getUsername() + " обнавлен в users");
    }
}
