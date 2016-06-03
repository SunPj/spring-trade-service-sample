package ru.sunsongs.auldanov.tradeservice.model;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * @author kraken
 * @since 6/2/16.
 */
public class OrderData {
    private long quantity;
    private BigDecimal price;

    public OrderData() {
    }

    public OrderData(long quantity, BigDecimal price) {
        this.quantity = quantity;
        this.price = price;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Min(1)
    public long getQuantity() {
        return quantity;
    }

    @Min(1)
    public BigDecimal getPrice() {
        return price;
    }
}
