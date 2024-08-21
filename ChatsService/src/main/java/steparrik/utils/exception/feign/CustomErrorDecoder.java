package steparrik.utils.exception.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import steparrik.utils.exception.ApiException;
import steparrik.utils.exception.ExceptionEntity;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.info("Exception {} {}", methodKey, response);
        if(methodKey.contains("getUserByUsernameOrPhoneNumber")){
            return new ApiException("Пользователь с данным ником или номером телефона не найден", HttpStatus.BAD_REQUEST);
        }
        return new Exception();
    }
}
