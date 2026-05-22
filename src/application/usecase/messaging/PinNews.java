package application.usecase.messaging;

import application.Result;
import domain.messaging.News;
import domain.repository.NewsRepository;
import domain.shared.Username;
import domain.user.Dean;
import domain.user.Manager;
import domain.user.User;
import infrastructure.logging.Logger;

public final class PinNews {
    private final NewsRepository news;
    private final Logger logger;

    public PinNews(NewsRepository news, Logger logger) {
        this.news = news;
        this.logger = logger;
    }

    public Result execute(User actor, int newsId, boolean pin) {
        News n = news.findById(newsId).orElse(null);
        if (n == null) return Result.fail("News not found.");

        boolean isAuthor    = n.author().equals(actor.username());
        boolean canOverride = actor instanceof Manager || actor instanceof Dean;
        if (!isAuthor && !canOverride) return Result.fail("Not authorised to change pin status.");

        if (pin) n.pin(); else n.unpin();
        news.save(n);
        logger.log(actor.username(), (pin ? "Pinned" : "Unpinned") + " news #" + newsId);
        return Result.ok(pin ? "News pinned." : "News unpinned.");
    }
}
