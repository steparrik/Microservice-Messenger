package steparrik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import steparrik.model.chat.Chat;
import steparrik.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findById(Long id);
    List<Chat> findAllByParticipants(User user);
}
