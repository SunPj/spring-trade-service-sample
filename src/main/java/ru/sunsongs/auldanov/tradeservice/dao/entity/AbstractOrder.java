package ru.sunsongs.auldanov.tradeservice.dao.entity;

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
    private int quantity;
    private BigDecimal price;
    private Date created;
    private long version;
    private Execution execution;

    @Min(0)
    @Column(name = "quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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

    @OneToMany(mappedBy = "executed_id")
    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }
}