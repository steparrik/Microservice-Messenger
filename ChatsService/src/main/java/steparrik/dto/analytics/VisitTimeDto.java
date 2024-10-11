package steparrik.dto.analytics;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitTimeDto {
    private Long userId;
    private LocalDateTime time;
}
