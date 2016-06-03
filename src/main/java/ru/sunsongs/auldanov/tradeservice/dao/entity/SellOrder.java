package ru.sunsongs.auldanov.tradeservice.dao.entity;

import ru.sunsongs.auldanov.tradeservice.model.OrderData;

import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author kraken
 * @since 6/2/16.
 */
@Entity
@Table(name = "sell_order")
public class SellOrder extends AbstractOrder {
    public SellOrder() {
    }

    public SellOrder(@Nonnull OrderData orderData) {
        super(orderData);
    }
}
