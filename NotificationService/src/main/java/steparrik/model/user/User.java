package steparrik.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import steparrik.model.chat.Chat;
import steparrik.model.message.Message;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String password;

    @NotEmpty
    @Column(unique = true)
    private String username;

    @NotEmpty
    private String phoneNumber;

    private String fullName;


    @OneToMany(mappedBy = "sender")
    private List<Message> messages;

    @ManyToMany(mappedBy = "participants")
    private List<Chat> chats;

    public User(String password, String username, String phoneNumber, String fullName) {
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
    }

}

