package steparrik.model.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import steparrik.model.message.Message;

import java.util.List;

@Entity
@Table(name = "chats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "chat_participants", joinColumns = @JoinColumn(name = "chat_id"))
    @Column(name = "user_id")
    private List<Long> participantsId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatType chatType;

    @OneToMany(mappedBy = "chat")
    @OrderBy("timestamp ASC")
    private List<Message> messages;

}
