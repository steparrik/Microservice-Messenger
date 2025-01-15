package steparrik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import steparrik.model.chat.Chat;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findById(Long id);

    List<Chat> findAllByParticipantsId(Long id);

    @Query(value = "SELECT chats.* " +
            "FROM chats " +
            "JOIN chat_participants ON chats.id = chat_participants.chat_id " +
            "LEFT JOIN messages ON chats.id = messages.chat_id " +
            "WHERE chat_participants.user_id = :participantId " +
            "GROUP BY chats.id, chats.name, chats.chat_type " +
            "ORDER BY MAX(messages.timestamp) DESC NULLS LAST", nativeQuery = true)
    List<Chat> findAllByParticipantsIdSort(@Param("participantId") Long participantId);
}
