package steparrik.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import steparrik.dto.user.RegistrationUserDto;
import steparrik.service.kafka.ProducerService;
import steparrik.service.RegistrationService;
import steparrik.service.UserService;
import steparrik.utils.exceptions.ExceptionEntity;
import steparrik.utils.validate.user.UserValidate;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationManager {
    private final RegistrationService registrationService;
    private final UserValidate userValidate;
    private final ProducerService producerService;

    public ResponseEntity<?> registration(@RequestBody RegistrationUserDto registrationUserDto) {
        ExceptionEntity exceptionEntity = userValidate.validateRegistrationDate(registrationUserDto);
        if(exceptionEntity!=null){
            return ResponseEntity.status(404).body(exceptionEntity);
        }else{
            producerService.save(registrationUserDto);
            registrationService.registration(registrationUserDto);
            return ResponseEntity.ok().body(null);
        }
    }

}
