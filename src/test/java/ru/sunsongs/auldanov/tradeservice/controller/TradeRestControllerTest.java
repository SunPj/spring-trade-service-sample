package ru.sunsongs.auldanov.tradeservice.controller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.sunsongs.auldanov.tradeservice.Application;
import ru.sunsongs.auldanov.tradeservice.dao.BuyOrderDao;
import ru.sunsongs.auldanov.tradeservice.dao.SellOrderDao;
import ru.sunsongs.auldanov.tradeservice.dao.entity.BuyOrder;
import ru.sunsongs.auldanov.tradeservice.dao.entity.SellOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author kraken
 * @since 6/3/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
public class TradeRestControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private SellOrderDao sellOrderDao;
    @Autowired
    private BuyOrderDao buyOrderDao;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void addSellOrderMustReturnOkIfDataIsValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sell").param("quantity", "10").param("price", "100"))
                .andExpect(status().isOk());
    }

    @Test
    public void addSellOrderMustSaveProperDataInDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sell").param("quantity", "10").param("price", "100"));

        assertThat(sellOrderDao.findAll(), Matchers.<List<SellOrder>>allOf(
                hasSize(1),
                Matchers.<SellOrder>hasItem(Matchers.allOf(
                        Matchers.<SellOrder>hasProperty("quantity", is(10L)),
                        Matchers.<SellOrder>hasProperty("price", is(BigDecimal.valueOf(100))),
                        Matchers.<SellOrder>hasProperty("remain", is(10L))
                ))
        ));
    }

    @Test
    public void addSellOrderMustReturn400IfDataIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sell").param("quantity", "10").param("price", "-100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addBuyOrderMustReturnOkIfDataIsValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/buy").param("quantity", "10").param("price", "100"))
                .andExpect(status().isOk());
    }

    @Test
    public void addBuyOrderMustSaveProperDataInDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/buy").param("quantity", "11").param("price", "101"));

        assertThat(buyOrderDao.findAll(), Matchers.<List<BuyOrder>>allOf(
                hasSize(1),
                Matchers.<BuyOrder>hasItem(Matchers.allOf(
                        Matchers.<BuyOrder>hasProperty("quantity", is(11L)),
                        Matchers.<BuyOrder>hasProperty("price", is(BigDecimal.valueOf(101))),
                        Matchers.<BuyOrder>hasProperty("remain", is(11L))
                ))
        ));
    }

    @Test
    public void addBuyOrderMustReturn400IfDataIsNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/buy").param("quantity", "-10").param("price", "11"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql("sellOrders.sql")
    public void waitingSellOrdersReturnsProperData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/waiting/sell"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(hasEntry("id", 111)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(hasEntry("price", 1.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(hasEntry("quantity", 11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(hasEntry("remain", 9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").value(hasEntry("id", 333)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").value(hasEntry("price", 3.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").value(hasEntry("quantity", 33)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").value(hasEntry("remain", 3)));
    }

    @Test
    @Sql("buyOrders.sql")
    public void waitingBuyOrdersReturnsProperData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/waiting/buy"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(hasEntry("id", 18)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(hasEntry("price", 81.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(hasEntry("quantity", 8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(hasEntry("remain", 1)));
    }

    @Test
    @Sql("executedOrders.sql")
    public void executedOrdersReturnsProperData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/executed"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(hasEntry("id", 22)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].buyOrder").value(Matchers.<Map<String, Number>>allOf(
                        hasEntry("id", 18),
                        hasEntry("price", 600.0),
                        hasEntry("quantity", 1000),
                        hasEntry("remain", 500)
                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sellOrder").value(Matchers.<Map<String, Number>>allOf(
                        hasEntry("id", 222),
                        hasEntry("price", 500.0),
                        hasEntry("quantity", 500),
                        hasEntry("remain", 0)
                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(hasEntry("quantity", 500)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").value(hasEntry("id", 11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].buyOrder").value(Matchers.<Map<String, Number>>allOf(
                        hasEntry("id", 17),
                        hasEntry("price", 100.0),
                        hasEntry("quantity", 100),
                        hasEntry("remain", 0)
                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sellOrder").value(Matchers.<Map<String, Number>>allOf(
                        hasEntry("id", 111),
                        hasEntry("price", 50.0),
                        hasEntry("quantity", 150),
                        hasEntry("remain", 50)
                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").value(hasEntry("quantity", 100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].created").exists());

    }

    @Test
    @Sql("executedOrders.sql")
    public void lastExecutionReturnsProperData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/executed/last"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyOrder").value(Matchers.<Map<String, Number>>allOf(
                        hasEntry("id", 18),
                        hasEntry("price", 600.0),
                        hasEntry("quantity", 1000),
                        hasEntry("remain", 500)
                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sellOrder").value(Matchers.<Map<String, Number>>allOf(
                        hasEntry("id", 222),
                        hasEntry("price", 500.0),
                        hasEntry("quantity", 500),
                        hasEntry("remain", 0)
                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(500))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created").exists());
    }
}