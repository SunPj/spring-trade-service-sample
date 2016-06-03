package ru.sunsongs.auldanov.tradeservice.service;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.sunsongs.auldanov.tradeservice.Application;
import ru.sunsongs.auldanov.tradeservice.dao.entity.Execution;
import ru.sunsongs.auldanov.tradeservice.model.OrderData;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author kraken
 * @since 6/3/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class ExecutionServiceTest {
    @Autowired
    private ExecutionService executionService;

    @Test
    public void testCase1() {
        assertThat(executionService.waitingSellOrders(), hasSize(0));
        executionService.sell(orderData(200, 20));
        assertThat(executionService.waitingSellOrders(), hasSize(1));
        executionService.sell(orderData(100, 10));
        assertThat(executionService.waitingSellOrders(), hasSize(2));
        assertThat(executionService.executedOrders(100), empty());
        executionService.buy(orderData(50, 5));
        assertThat(executionService.executedOrders(100), empty());
        executionService.buy(orderData(50, 30));
        assertThat(executionService.executedOrders(100), Matchers.<List<Execution>>allOf(
                hasSize(1),
                hasItem(allOf(hasProperty("quantity", is(50L)),
                        hasProperty("buyOrder", allOf(
                                hasProperty("price", is(BigDecimal.valueOf(30))),
                                hasProperty("quantity", is(50L)),
                                hasProperty("remain", is(0L))
                        )),
                        hasProperty("sellOrder", allOf(
                                hasProperty("price", is(BigDecimal.valueOf(20))),
                                hasProperty("quantity", is(200L)),
                                hasProperty("remain", is(150L))
                        ))
                ))
        ));
    }

    @Test
    public void testCase2() {
        assertThat(executionService.waitingBuyOrders(), hasSize(0));
        executionService.buy(orderData(10, 2));
        assertThat(executionService.waitingBuyOrders(), hasSize(1));
        executionService.buy(orderData(20, 5));
        assertThat(executionService.waitingBuyOrders(), hasSize(2));
        executionService.buy(orderData(30, 15));
        assertThat(executionService.waitingBuyOrders(), hasSize(3));
        assertThat(executionService.executedOrders(100), empty());
        executionService.sell(orderData(40, 20));
        assertThat(executionService.executedOrders(100), empty());
        executionService.sell(orderData(30, 5));
        assertThat(executionService.executedOrders(100), Matchers.<List<Execution>>allOf(
                hasSize(2),
                hasItem(allOf(hasProperty("quantity", is(20L)),
                        hasProperty("buyOrder", allOf(
                                hasProperty("price", is(BigDecimal.valueOf(5))),
                                hasProperty("quantity", is(20L)),
                                hasProperty("remain", is(0L))
                        )),
                        hasProperty("sellOrder", allOf(
                                hasProperty("price", is(BigDecimal.valueOf(5))),
                                hasProperty("quantity", is(30L)),
                                hasProperty("remain", is(0L))
                        ))
                )),
                hasItem(allOf(hasProperty("quantity", is(10L)),
                        hasProperty("buyOrder", allOf(
                                hasProperty("price", is(BigDecimal.valueOf(15))),
                                hasProperty("quantity", is(30L)),
                                hasProperty("remain", is(20L))
                        )),
                        hasProperty("sellOrder", allOf(
                                hasProperty("price", is(BigDecimal.valueOf(5))),
                                hasProperty("quantity", is(30L)),
                                hasProperty("remain", is(0L))
                        ))
                ))
        ));
    }

    private OrderData orderData(long quantity, long price) {
        return new OrderData(quantity, BigDecimal.valueOf(price));
    }
}