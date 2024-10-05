package steparrik.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import steparrik.dto.user.EditUserKafkaDto;
import steparrik.dto.user.RegistrationUserDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void save(RegistrationUserDto registrationUserDto) {
        kafkaTemplate.send("save-topic", registrationUserDto);
    }

    public void edit(EditUserKafkaDto editUserKafkaDto) {
        kafkaTemplate.send("edit-topic", editUserKafkaDto);
    }




}
