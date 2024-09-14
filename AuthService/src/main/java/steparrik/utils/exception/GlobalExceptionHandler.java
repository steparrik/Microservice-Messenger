package steparrik.utils.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionEntity> handleValidationExceptions(MethodArgumentNotValidException ex) {
        LocalDateTime nowTime = LocalDateTime.now();
        List<ExceptionEntity> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(er -> {
                    ExceptionEntity exceptionEntity = new ExceptionEntity();
                    exceptionEntity.setMessage(er.getDefaultMessage());
                    exceptionEntity.setLocalDateTime(nowTime);
                    return exceptionEntity;
                })
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errors.get(0));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionEntity> handleValidationExceptions(ApiException ex) {
        ExceptionEntity exceptionEntity = new ExceptionEntity();
        exceptionEntity.setMessage(ex.getMessage());
        exceptionEntity.setLocalDateTime(ex.getTimestamp());
        return ResponseEntity.status(ex.getStatus()).body(exceptionEntity);
    }


}
