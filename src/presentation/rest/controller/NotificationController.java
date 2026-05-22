package presentation.rest.controller;

import bootstrap.AppContext;
import domain.messaging.Notification;
import domain.user.User;
import infrastructure.persistence.json.JsonObjectBuilder;
import infrastructure.persistence.json.JsonValue;
import presentation.rest.auth.RequestContext;
import presentation.rest.http.HttpRequest;
import presentation.rest.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

/** Handles notification retrieval and clearing for the current user. */
public final class NotificationController {
    private final AppContext ctx;

    public NotificationController(AppContext ctx) {
        this.ctx = ctx;
    }

    /** GET /api/notifications */
    public HttpResponse list(HttpRequest request) {
        User user = RequestContext.current();
        List<Notification> notes = ctx.notificationRepository.findFor(user.username());
        List<JsonValue> arr = new ArrayList<>();
        for (Notification n : notes) {
            arr.add(JsonObjectBuilder.create()
                    .put("text", n.text())
                    .put("at",   n.at().toString())
                    .build());
        }
        return HttpResponse.ok(new JsonValue.JsonArray(arr));
    }

    /** DELETE /api/notifications — clear all notifications for current user */
    public HttpResponse clear(HttpRequest request) {
        User user = RequestContext.current();
        ctx.notificationRepository.clearFor(user.username());
        return HttpResponse.ok(JsonObjectBuilder.create().put("message", "Cleared.").build());
    }
}
