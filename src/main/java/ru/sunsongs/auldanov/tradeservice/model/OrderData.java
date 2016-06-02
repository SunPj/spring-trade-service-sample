package ru.sunsongs.auldanov.tradeservice.model;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * @author kraken
 * @since 6/2/16.
 */
public class OrderData {
    private int quantity;
    private BigDecimal price;

    public OrderData(int quantity, BigDecimal price) {
        this.quantity = quantity;
        this.price = price;
    }

    @Size(min = 1)
    public int getQuantity() {
        return quantity;
    }

    @Size(min = 1)
    public BigDecimal getPrice() {
        return price;
    }
}
