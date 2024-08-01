package steparrik.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import steparrik.dto.token.JwtResponseDto;
import steparrik.dto.user.EditUserDto;
import steparrik.dto.user.EditUserKafkaDto;
import steparrik.model.user.User;
import steparrik.service.UserDetailService;
import steparrik.service.UserService;
import steparrik.service.kafka.ProducerService;
import steparrik.utils.exceptions.ExceptionEntity;
import steparrik.utils.token.JwtTokenUtil;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EditManager {
    private final UserService userService;
    private final UserDetailService userDetailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final ProducerService producerService;

    public ResponseEntity<?> editUser(EditUserDto editUserDto, String username) {
        EditUserKafkaDto editUserKafkaDto = new EditUserKafkaDto();
        editUserKafkaDto.setOldUsername(username);
        Optional<User> user = userService.findUserByUsername(username);

        if(editUserDto.getUsername()!=null && !editUserDto.getUsername().isEmpty()){
            if(userService.findUserByUsername(editUserDto.getUsername()).isPresent()){
                return new ResponseEntity<>(new ExceptionEntity("Ник занят другим пользователем", LocalDateTime.now()), HttpStatus.NOT_FOUND);
            }
            user.get().setUsername(editUserDto.getUsername());
            editUserKafkaDto.setUsername(editUserDto.getUsername());
        }
        if(editUserDto.getFullName()!=null && !editUserDto.getFullName().isEmpty()){
            editUserKafkaDto.setFullName(editUserDto.getFullName());
        }
        if(editUserDto.getPassword()!=null && !editUserDto.getPassword().isEmpty()){
            String encodePassword = bCryptPasswordEncoder.encode(editUserDto.getPassword());
            user.get().setPassword(encodePassword);
            editUserKafkaDto.setPassword(encodePassword);
        }

        producerService.edit(editUserKafkaDto);
        userService.save(user.get());
        UserDetails userDetails = userDetailService.loadUserByUsername(user.get().getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }
}
