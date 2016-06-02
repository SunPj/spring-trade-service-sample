package ru.sunsongs.auldanov.tradeservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.sunsongs.auldanov.tradeservice.dao.BuyOrderDao;
import ru.sunsongs.auldanov.tradeservice.dao.ExecutedOrderDao;
import ru.sunsongs.auldanov.tradeservice.dao.SellOrderDao;
import ru.sunsongs.auldanov.tradeservice.dao.entity.BuyOrder;
import ru.sunsongs.auldanov.tradeservice.dao.entity.Execution;
import ru.sunsongs.auldanov.tradeservice.dao.entity.SellOrder;
import ru.sunsongs.auldanov.tradeservice.model.OrderData;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * @author kraken
 * @since 4/25/16.
 */
@ThreadSafe
@Service
@Transactional
public class ExecutionService {
    private final SellOrderDao sellOrderDao;
    private final BuyOrderDao buyOrderDao;
    private final ExecutedOrderDao executedOrderDao;

    @Autowired
    public ExecutionService(@Nonnull ExecutedOrderDao executedOrderDao,
                            SellOrderDao sellOrderDao,
                            BuyOrderDao buyOrderDao) {
        this.sellOrderDao = requireNonNull(sellOrderDao);
        this.buyOrderDao = requireNonNull(buyOrderDao);
        this.executedOrderDao = requireNonNull(executedOrderDao);
    }

    public List<BuyOrder> waitingBuyOrders() {
        return buyOrderDao.findByReminderGreaterThan0();
    }

    public List<SellOrder> waitingSellOrders() {
        return sellOrderDao.findByReminderGreaterThan0();
    }

    public void sell(OrderData sellRequest) {
        // TODO save order, find pair for execution for new order
    }

    public void buy(OrderData buyRequest) {
        // TODO save order, find pair for execution for new order
    }

    public List<Execution> executedOrders(int count) {
        return executedOrderDao.findOrderByIdDesc(new PageRequest(1, count));
    }

    public Optional<Execution> lastExecution() {
        return executedOrderDao.findTop1OrderByIdDesc();
    }
}