package steparrik.model.message;

import jakarta.persistence.*;
import lombok.*;
import steparrik.model.chat.Chat;
import steparrik.model.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String messageText;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chats;
}
