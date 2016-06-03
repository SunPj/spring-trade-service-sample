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

    @Autowired
    public ExecutionService(@Nonnull ExecutedOrderDao executedOrderDao,
                            @Nonnull SellOrderDao sellOrderDao,
                            @Nonnull BuyOrderDao buyOrderDao) {
        this.sellOrderDao = requireNonNull(sellOrderDao);
        this.buyOrderDao = requireNonNull(buyOrderDao);
        this.executedOrderDao = requireNonNull(executedOrderDao);
    }

    public List<BuyOrder> waitingBuyOrders() {
        return buyOrderDao.findByRemindGreaterThan(0);
    }

    public List<SellOrder> waitingSellOrders() {
        return sellOrderDao.findByRemindGreaterThan(0);
    }

    private void execute(SellOrder sellOrder, BuyOrder buyOrder) {
        long quantity = buyOrder.getRemind() > sellOrder.getRemind() ? sellOrder.getRemind() : buyOrder.getRemind();
        final Execution execution = new Execution(buyOrder, sellOrder, quantity);
        executedOrderDao.save(execution);
        buyOrder.setRemind(buyOrder.getRemind() - quantity);
        sellOrder.setRemind(sellOrder.getRemind() - quantity);

    }

    public void sell(OrderData sellRequest) {
        final SellOrder sellOrder = new SellOrder(sellRequest);
        sellOrderDao.save(sellOrder);
        List<BuyOrder> buyOrders = buyOrderDao.getSuitableOrders(sellOrder.getPrice());
        for (BuyOrder buyOrder : buyOrders) {
            execute(sellOrder, buyOrder);
            if (sellOrder.getQuantity() == 0) {
                break;
            }
        }
    }

    public void buy(OrderData buyRequest) {
        final BuyOrder buyOrder = new BuyOrder(buyRequest);
        buyOrderDao.save(buyOrder);
        List<SellOrder> sellOrders = sellOrderDao.getSuitableOrders(buyOrder.getPrice());
        for (SellOrder sellOrder : sellOrders) {
            execute(sellOrder, buyOrder);
            if (buyOrder.getQuantity() == 0) {
                break;
            }
        }
    }

    public List<Execution> executedOrders(int count) {
        return executedOrderDao.findAllByOrderByIdDesc(new PageRequest(1, count));
    }

    public Execution lastExecution() {
        return executedOrderDao.findTopByOrderByIdDesc();
    }
}