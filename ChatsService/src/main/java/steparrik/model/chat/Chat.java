package steparrik.model.chat;

import jakarta.persistence.*;
import lombok.*;
import steparrik.model.message.Message;
import steparrik.model.user.User;

import java.util.List;

@Entity
@Table(name = "chats")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "chats"),
            inverseJoinColumns = @JoinColumn(name ="participants"))
    private List<User> participants;

    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

}