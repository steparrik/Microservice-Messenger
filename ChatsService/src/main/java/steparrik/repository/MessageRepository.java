package steparrik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import steparrik.model.message.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
}
