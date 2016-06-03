package ru.sunsongs.auldanov.tradeservice.dao.entity;

import ru.sunsongs.auldanov.tradeservice.model.OrderData;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author kraken
 * @since 6/2/16.
 */
@MappedSuperclass
abstract class AbstractOrder extends AbstractEntity {
    private long quantity;
    private long remain;
    private BigDecimal price;
    private Date created;
    private long version;

    public AbstractOrder() {
    }

    public AbstractOrder(@Nonnull OrderData orderData) {
        this.created = new Date();
        this.price = orderData.getPrice();
        this.quantity = orderData.getQuantity();
        this.remain = this.quantity;
    }

    @Min(0)
    @Column(name = "quantity")
    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Min(0)
    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Version
    @Column(name = "version")
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

     @Column(name = "remain")
    public long getRemain() {
        return remain;
    }

    public void setRemain(long remain) {
        this.remain = remain;
    }
}