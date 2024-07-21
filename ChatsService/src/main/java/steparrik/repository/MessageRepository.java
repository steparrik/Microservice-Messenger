package steparrik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import steparrik.model.message.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}