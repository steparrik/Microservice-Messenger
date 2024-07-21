package steparrik.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

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
    @Column(unique = true)
    private String phoneNumber;

    private String fullName;

    public User(String password, String username, String phoneNumber, String fullName) {
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
    }
}

