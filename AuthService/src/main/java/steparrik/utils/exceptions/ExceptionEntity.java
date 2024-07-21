package steparrik.utils.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExceptionEntity {
    private String message;
    private LocalDateTime localDateTime;
}
