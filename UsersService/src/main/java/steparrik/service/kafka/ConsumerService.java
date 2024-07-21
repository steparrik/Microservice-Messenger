package steparrik.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import steparrik.dto.user.RegistrationUserDto;
import steparrik.model.user.User;
import steparrik.service.UserService;
import steparrik.utils.mapper.user.RegistrationUserMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {
    private final UserService userService;


    @KafkaListener(topics = "save-topic", groupId = "A")
    public void saveListener(RegistrationUserDto registrationUserDto) {
        RegistrationUserMapper registrationUserMapper = new RegistrationUserMapper();
        userService.save(registrationUserMapper.toEntity(registrationUserDto));
        log.info(registrationUserDto.getUsername() + " добавлен в users");
    }
}