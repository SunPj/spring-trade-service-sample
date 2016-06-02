package ru.sunsongs.auldanov.tradeservice.dao.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author kraken
 * @since 6/2/16.
 */
@Entity
@Table(name = "execution")
public class Execution extends AbstractEntity {
    private BuyOrder buyOrder;
    private SellOrder sellOrder;
    private Date created;
    private long quantity;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "buy_order_id")
    public BuyOrder getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(BuyOrder buyOrder) {
        this.buyOrder = buyOrder;
    }

    @NotNull
    @JoinColumn(name = "sell_order_id")
    public SellOrder getSellOrder() {
        return sellOrder;
    }

    public void setSellOrder(SellOrder sellOrder) {
        this.sellOrder = sellOrder;
    }

    @Min(0)
    @Column(name = "quantity")
    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}