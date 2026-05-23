package presentation.cli.menu;

import application.Result;
import application.usecase.messaging.ProcessRequest;
import domain.enums.RequestStatus;
import domain.repository.RequestRepository;
import domain.user.Dean;
import presentation.cli.Console;

import java.util.ArrayList;
import java.util.List;

public final class DeanMenu extends Menu {
    private final Dean dean;
    private final RequestRepository requests;
    private final ProcessRequest processRequest;
    private final BecomeResearcherAction becomeResearcher;
    private final ResearcherMenuExtension researcherMenu;
    private final CommonMenuActions common;

    public DeanMenu(Console console, Dean dean, RequestRepository requests,
                    ProcessRequest processRequest,
                    BecomeResearcherAction becomeResearcher, ResearcherMenuExtension researcherMenu,
                    CommonMenuActions common) {
        super(console);
        this.dean = dean;
        this.requests = requests;
        this.processRequest = processRequest;
        this.becomeResearcher = becomeResearcher;
        this.researcherMenu = researcherMenu;
        this.common = common;
    }

    @Override protected String title() { return "=== DEAN MENU (" + dean.username() + ") ==="; }

    @Override protected List<MenuItem> items() {
        List<MenuItem> items = new ArrayList<>();
        if (!dean.isResearcher())
            items.add(new MenuItem("Become a researcher", () -> becomeResearcher.run(dean)));
        items.add(new MenuItem("View all pending requests", this::viewAllPending));
        items.add(new MenuItem("Approve a request",         () -> decide(RequestStatus.APPROVED)));
        items.add(new MenuItem("Reject a request",          () -> decide(RequestStatus.NOT_APPROVED)));
        items.add(new MenuItem("View inbox",                common::viewInbox));
        items.add(new MenuItem("Send message",              common::sendMessageInteractive));
        items.add(new MenuItem("View news",                 common::viewNews));
        items.add(new MenuItem("Publish news",              common::publishNewsInteractive));
        items.addAll(researcherMenu.itemsFor(dean));
        return items;
    }

    private void viewAllPending() {
        var pending = requests.findAll().stream()
                .filter(r -> r.status() == RequestStatus.PENDING).toList();
        if (pending.isEmpty()) { console.println("No pending requests."); return; }
        pending.forEach(r -> console.println(
                "  [" + r.id() + "] " + r.title() + " — " + r.requester() + " | " + r.type() + " | " + r.urgency()));
    }

    private void decide(RequestStatus status) {
        viewAllPending();
        int id = console.readInt("Request ID:");
        Result r = processRequest.execute(dean.username(), id, status);
        console.println(r.message());
    }
}
