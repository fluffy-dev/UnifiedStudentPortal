package presentation.cli.menu;

import application.usecase.messaging.CommentOnNews;
import application.usecase.messaging.CreateITOrder;
import application.usecase.messaging.PublishNews;
import application.usecase.messaging.SendMessage;
import application.usecase.messaging.SubmitRequest;
import domain.enums.HelpType;
import domain.enums.UrgencyLevel;
import domain.messaging.Message;
import domain.messaging.News;
import domain.repository.MessageRepository;
import domain.repository.NewsRepository;
import domain.shared.Username;
import domain.user.User;
import presentation.cli.Console;

import java.util.List;

/**
 * Shared CLI actions available to all (or most) roles:
 * messaging inbox/send, news reading, help requests, IT orders.
 * Instantiated per-menu so each menu can expose whichever subset it needs.
 */
public final class CommonMenuActions {
    private final Console console;
    private final User user;
    private final MessageRepository messages;
    private final NewsRepository news;
    private final SendMessage sendMessage;
    private final CommentOnNews commentOnNews;
    private final PublishNews publishNews;
    private final SubmitRequest submitRequest;
    private final CreateITOrder createOrder;

    public CommonMenuActions(Console console, User user,
                      MessageRepository messages, NewsRepository news,
                      SendMessage sendMessage, CommentOnNews commentOnNews,
                      PublishNews publishNews,
                      SubmitRequest submitRequest, CreateITOrder createOrder) {
        this.console       = console;
        this.user          = user;
        this.messages      = messages;
        this.news          = news;
        this.sendMessage   = sendMessage;
        this.commentOnNews = commentOnNews;
        this.publishNews   = publishNews;
        this.submitRequest = submitRequest;
        this.createOrder   = createOrder;
    }

    // ── Inbox ────────────────────────────────────────────────────────────────

    void viewInbox() {
        List<Message> inbox = messages.inboxOf(user.username());
        if (inbox.isEmpty()) { console.println("Inbox is empty."); return; }
        console.println("\n--- INBOX (" + inbox.size() + " messages) ---");
        for (int i = 0; i < inbox.size(); i++) {
            Message m = inbox.get(i);
            String status = m.status().name();
            console.println((i + 1) + ". [" + status + "] [" + m.urgency() + "] " + m.subject()
                    + " — from " + m.sender());
        }
        int choice = console.readInt("Read message (0 = back):");
        if (choice < 1 || choice > inbox.size()) return;
        Message m = inbox.get(choice - 1);
        console.println("\nFrom:    " + m.sender());
        console.println("Subject: " + m.subject());
        console.println("Urgency: " + m.urgency());
        console.println("Sent:    " + m.sentAt());
        console.println("\n" + m.body());
        if (m.status().name().equals("UNREAD")) {
            m.markRead();
            messages.save(m);
        }
    }

    void sendMessageInteractive() {
        String to      = console.readLine("To (username):");
        String subject = console.readLine("Subject:");
        String body    = console.readLine("Body:");
        String urgStr  = console.readLine("Urgency (LOW/MEDIUM/HIGH):");
        try {
            UrgencyLevel level = UrgencyLevel.valueOf(urgStr.toUpperCase());
            var r = sendMessage.execute(user.username(), new Username(to), subject, body, level);
            console.println(r.message());
        } catch (IllegalArgumentException e) {
            console.println("Invalid urgency. Use LOW, MEDIUM, or HIGH.");
        }
    }

    // ── News ─────────────────────────────────────────────────────────────────

    void viewNews() {
        List<News> all = news.findAllSorted();
        if (all.isEmpty()) { console.println("No news."); return; }
        console.println("\n--- NEWS ---");
        for (int i = 0; i < all.size(); i++) {
            News n = all.get(i);
            String pin = n.isPinned() ? "[PIN] " : "";
            console.println((i + 1) + ". " + pin + n.title() + " — by " + n.author());
        }
        int choice = console.readInt("Read post (0 = back):");
        if (choice < 1 || choice > all.size()) return;
        News n = all.get(choice - 1);
        console.println("\n=== " + n.title() + " ===");
        console.println("By: " + n.author() + " | " + n.publishedAt());
        console.println("\n" + n.body());
        if (!n.comments().isEmpty()) {
            console.println("\n--- Comments ---");
            n.comments().forEach(c -> console.println(c.author() + ": " + c.text()));
        }
        String reply = console.readLine("Leave a comment (enter = skip):");
        if (!reply.isBlank()) {
            var r = commentOnNews.execute(user, n.id(), reply);
            console.println(r.message());
        }
    }

    void publishNewsInteractive() {
        String title  = console.readLine("Title:");
        String body   = console.readLine("Body:");
        String pinStr = console.readLine("Pin this post? (y/n):");
        boolean pin   = pinStr.equalsIgnoreCase("y");
        if (user instanceof domain.user.Employee emp) {
            var r = publishNews.execute(emp.username(), title, body, pin);
            console.println(r.message());
        } else {
            console.println("Only employees can publish news.");
        }
    }

    // ── Help requests ─────────────────────────────────────────────────────────

    void submitRequestInteractive() {
        console.println("Request types: TRANSCRIPT_FOR_SEMESTER, TRANSCRIPT_FOR_YEAR, " +
                "CERTIFICATE_OF_EDUCATION, ACADEMIC_MOBILITY, COORDINATION_OF_DIPLOMA_TOPIC, " +
                "REQUEST_FOR_CREATING_ORGANIZATION");
        String typeStr    = console.readLine("Type:");
        String title      = console.readLine("Title:");
        String body       = console.readLine("Description:");
        String urgencyStr = console.readLine("Urgency (LOW/MEDIUM/HIGH):");
        try {
            HelpType     type    = HelpType.valueOf(typeStr.toUpperCase());
            UrgencyLevel urgency = UrgencyLevel.valueOf(urgencyStr.toUpperCase());
            var r = submitRequest.execute(user, title, type, urgency, body);
            console.println(r.message());
        } catch (IllegalArgumentException e) {
            console.println("Invalid type or urgency.");
        }
    }

    // ── IT orders ────────────────────────────────────────────────────────────

    void createOrderInteractive() {
        String desc = console.readLine("Describe the issue:");
        if (desc.isBlank()) { console.println("Description required."); return; }
        var r = createOrder.execute(user, desc);
        console.println(r.message());
    }
}
