package presentation.cli.menu;

import application.Result;
import application.usecase.messaging.AcceptOrder;
import application.usecase.messaging.CompleteOrder;
import domain.enums.OrderStatus;
import domain.repository.OrderRepository;
import domain.user.TechSupport;
import presentation.cli.Console;

import java.util.ArrayList;
import java.util.List;

public final class TechSupportMenu extends Menu {
    private final TechSupport tech;
    private final OrderRepository orders;
    private final AcceptOrder acceptOrder;
    private final CompleteOrder completeOrder;
    private final CommonMenuActions common;

    public TechSupportMenu(Console console, TechSupport tech, OrderRepository orders,
                           AcceptOrder acceptOrder, CompleteOrder completeOrder,
                           CommonMenuActions common) {
        super(console);
        this.tech = tech;
        this.orders = orders;
        this.acceptOrder = acceptOrder;
        this.completeOrder = completeOrder;
        this.common = common;
    }

    @Override protected String title() { return "=== TECH SUPPORT MENU (" + tech.username() + ") ==="; }

    @Override protected List<MenuItem> items() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("View all orders",   this::viewAllOrders));
        items.add(new MenuItem("Accept an order",   this::acceptInteractive));
        items.add(new MenuItem("Complete an order", this::completeInteractive));
        items.add(new MenuItem("Reject an order",   this::rejectInteractive));
        items.add(new MenuItem("View inbox",        common::viewInbox));
        items.add(new MenuItem("Send message",      common::sendMessageInteractive));
        return items;
    }

    private void viewAllOrders() {
        var all = orders.findAll();
        if (all.isEmpty()) { console.println("No orders."); return; }
        console.println("--- NEW ---");
        all.stream().filter(o -> o.status() == OrderStatus.NEW)
           .forEach(o -> console.println("  [" + o.id() + "] " + o.description() + " — " + o.requester()));
        console.println("--- ACCEPTED ---");
        all.stream().filter(o -> o.status() == OrderStatus.ACCEPTED)
           .forEach(o -> console.println("  [" + o.id() + "] " + o.description() + " — " + o.requester()));
        console.println("--- DONE ---");
        all.stream().filter(o -> o.status() == OrderStatus.DONE)
           .forEach(o -> console.println("  [" + o.id() + "] " + o.description()));
    }

    private void acceptInteractive() {
        int id = console.readInt("Order ID:");
        Result r = acceptOrder.execute(tech, id);
        console.println(r.message());
    }

    private void completeInteractive() {
        int id = console.readInt("Order ID:");
        Result r = completeOrder.execute(tech, id);
        console.println(r.message());
    }

    private void rejectInteractive() {
        int id = console.readInt("Order ID:");
        orders.findById(id).ifPresentOrElse(o -> {
            o.reject();
            orders.save(o);
            console.println("Order #" + id + " rejected.");
        }, () -> console.println("Order not found."));
    }
}
