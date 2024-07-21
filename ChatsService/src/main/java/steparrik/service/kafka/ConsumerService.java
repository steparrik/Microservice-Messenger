package steparrik.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import steparrik.dto.user.RegistrationUserDto;
import steparrik.service.UserService;
import steparrik.utils.mapper.user.RegistrationUserMapper;


@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {
    private final UserService userService;

    @KafkaListener(topics = "save-topic", groupId = "B")
    public void saveListener(RegistrationUserDto registrationUserDto) {
        RegistrationUserMapper registrationUserMapper = new RegistrationUserMapper();
        userService.save(registrationUserMapper.toEntity(registrationUserDto));
        log.info(registrationUserDto.getUsername() + " добавлен в users");
    }


}
