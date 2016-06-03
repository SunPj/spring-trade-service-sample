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
    private final NotificationService notificationService;

    @Autowired
    public ExecutionService(@Nonnull ExecutedOrderDao executedOrderDao,
                            @Nonnull SellOrderDao sellOrderDao,
                            @Nonnull BuyOrderDao buyOrderDao,
                            NotificationService notificationService) {
        this.sellOrderDao = requireNonNull(sellOrderDao);
        this.buyOrderDao = requireNonNull(buyOrderDao);
        this.executedOrderDao = requireNonNull(executedOrderDao);
        this.notificationService = requireNonNull(notificationService);
    }

    public List<BuyOrder> waitingBuyOrders() {
        return buyOrderDao.findByRemainGreaterThan(0);
    }

    public List<SellOrder> waitingSellOrders() {
        return sellOrderDao.findByRemainGreaterThan(0);
    }

    private void execute(SellOrder sellOrder, BuyOrder buyOrder) {
        long quantity = buyOrder.getRemain() > sellOrder.getRemain() ? sellOrder.getRemain() : buyOrder.getRemain();
        buyOrder.setRemain(buyOrder.getRemain() - quantity);
        sellOrder.setRemain(sellOrder.getRemain() - quantity);
        final Execution execution = new Execution(buyOrder, sellOrder, quantity);
        executedOrderDao.save(execution);
        notificationService.newExecution(execution);
    }

    public void sell(OrderData sellRequest) {
        final SellOrder sellOrder = new SellOrder(sellRequest);
        sellOrderDao.save(sellOrder);
        notificationService.newSellOrder(sellOrder);
        List<BuyOrder> buyOrders = buyOrderDao.getSuitableOrders(sellOrder.getPrice());
        for (BuyOrder buyOrder : buyOrders) {
            execute(sellOrder, buyOrder);
            if (sellOrder.getRemain() == 0) {
                break;
            }
        }
    }

    public void buy(OrderData buyRequest) {
        final BuyOrder buyOrder = new BuyOrder(buyRequest);
        buyOrderDao.save(buyOrder);
        notificationService.newBuyOrder(buyOrder);
        List<SellOrder> sellOrders = sellOrderDao.getSuitableOrders(buyOrder.getPrice());
        for (SellOrder sellOrder : sellOrders) {
            execute(sellOrder, buyOrder);
            if (buyOrder.getRemain() == 0) {
                break;
            }
        }
    }

    public List<Execution> executedOrders(int count) {
        return executedOrderDao.findAllByOrderByIdDesc(new PageRequest(0, count));
    }

    public Execution lastExecution() {
        return executedOrderDao.findTopByOrderByIdDesc();
    }
}