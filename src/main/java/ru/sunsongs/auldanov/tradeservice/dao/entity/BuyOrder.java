package ru.sunsongs.auldanov.tradeservice.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author kraken
 * @since 6/2/16.
 */
@Entity
@Table(name = "buy_order")
public class BuyOrder extends AbstractOrder {
}
