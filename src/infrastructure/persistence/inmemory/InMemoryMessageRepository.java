package infrastructure.persistence.inmemory;

import domain.messaging.Message;
import domain.repository.MessageRepository;
import domain.shared.Username;

import java.util.ArrayList;
import java.util.List;

public final class InMemoryMessageRepository implements MessageRepository {
    private final List<Message> messages = new ArrayList<>();

    @Override public void save(Message m) { messages.add(m); }
    @Override public java.util.Optional<Message> findById(int id) {
        return messages.stream().filter(m -> m.id() == id).findFirst();
    }
    @Override public List<Message> inboxOf(Username recipient) {
        return messages.stream()
                .filter(m -> m.recipient().equals(recipient))
                .sorted()
                .toList();
    }
    @Override public List<Message> sentBy(Username sender) {
        return messages.stream()
                .filter(m -> m.sender().equals(sender))
                .sorted()
                .toList();
    }
}
