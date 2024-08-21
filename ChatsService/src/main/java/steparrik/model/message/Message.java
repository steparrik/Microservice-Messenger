package steparrik.model.message;

import jakarta.persistence.*;
import lombok.*;
import steparrik.model.chat.Chat;


import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String messageText;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private Long senderId;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;
}
