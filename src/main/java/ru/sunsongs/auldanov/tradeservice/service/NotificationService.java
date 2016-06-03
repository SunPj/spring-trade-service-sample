package ru.sunsongs.auldanov.tradeservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.sunsongs.auldanov.tradeservice.dao.entity.BuyOrder;
import ru.sunsongs.auldanov.tradeservice.dao.entity.Execution;
import ru.sunsongs.auldanov.tradeservice.dao.entity.SellOrder;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import static java.util.Objects.requireNonNull;

/**
 * @author kraken
 * @since 6/3/16.
 */
@ThreadSafe
@Service
public class NotificationService {
    private final SimpMessagingTemplate template;
    private static final String WEBSOCKET_NEW_BUY = "/topic/newBuyOrder";
    private static final String WEBSOCKET_NEW_SELL = "/topic/newSellOrder";
    private static final String WEBSOCKET_NEW_EXECUTION = "/topic/newExecution";

    @Autowired
    public NotificationService(@Nonnull SimpMessagingTemplate template) {
        this.template = requireNonNull(template);
    }

    public void newExecution(@Nonnull Execution execution) {
        template.convertAndSend(WEBSOCKET_NEW_EXECUTION, execution);
    }

    public void newSellOrder(@Nonnull SellOrder sellOrder) {
        template.convertAndSend(WEBSOCKET_NEW_SELL, sellOrder);
    }

    public void newBuyOrder(@Nonnull BuyOrder buyOrder) {
        template.convertAndSend(WEBSOCKET_NEW_BUY, buyOrder);
    }
}
