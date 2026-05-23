package domain.repository;

import domain.messaging.Message;
import domain.shared.Username;

import java.util.List;

public interface MessageRepository {
    void save(Message message);
    java.util.Optional<Message> findById(int id);
    List<Message> inboxOf(Username recipient);
    List<Message> sentBy(Username sender);
}
