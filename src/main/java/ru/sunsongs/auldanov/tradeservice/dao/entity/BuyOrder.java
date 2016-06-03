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
@Table(name = "buy_order")
public class BuyOrder extends AbstractOrder {
    public BuyOrder() {
    }

    public BuyOrder(@Nonnull OrderData orderData) {
        super(orderData);
    }
}
