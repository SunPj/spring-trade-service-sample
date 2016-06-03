package ru.sunsongs.auldanov.tradeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.sunsongs.auldanov.tradeservice.dao.entity.BuyOrder;
import ru.sunsongs.auldanov.tradeservice.dao.entity.Execution;
import ru.sunsongs.auldanov.tradeservice.dao.entity.SellOrder;
import ru.sunsongs.auldanov.tradeservice.model.OrderData;
import ru.sunsongs.auldanov.tradeservice.service.ExecutionService;

import javax.annotation.concurrent.ThreadSafe;
import javax.validation.Valid;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author kraken
 */
@ThreadSafe
@RestController
public class TradeRestController {
    private final ExecutionService executionService;

    @Autowired
    public TradeRestController(ExecutionService executionService) {
        this.executionService = requireNonNull(executionService);
    }

    @RequestMapping(value = "/api/sell", method = POST)
    @ResponseStatus(OK)
    public void sell(@Valid OrderData sellRequest) {
        executionService.sell(sellRequest);
    }

    @RequestMapping(value = "/api/buy", method = POST)
    @ResponseStatus(OK)
    public void buy(@Valid OrderData buyRequest) {
        executionService.buy(buyRequest);
    }

    @RequestMapping(value = "/api/order/waiting/sell", method = GET)
    public List<SellOrder> waitingSellOrders() {
        return executionService.waitingSellOrders();
    }

    @RequestMapping(value = "/api/order/waiting/buy", method = GET)
    public List<BuyOrder> waitingBuyOrders() {
        return executionService.waitingBuyOrders();
    }

    @RequestMapping(value = "/api/order/executed", method = GET)
    public List<Execution> executedOrders(@RequestParam(defaultValue = "10") int count) {
        return executionService.executedOrders(count);
    }

    @RequestMapping(value = "/api/order/executed/last", method = GET)
    public Execution lastExecution() {
        return executionService.lastExecution();
    }
}