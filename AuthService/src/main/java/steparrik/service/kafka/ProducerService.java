package steparrik.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import steparrik.dto.user.RegistrationUserDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {
    private final KafkaTemplate<String, RegistrationUserDto> kafkaTemplate;

    public void save(RegistrationUserDto registrationUserDto) {
        kafkaTemplate.send("save-topic", registrationUserDto);
    }



}
