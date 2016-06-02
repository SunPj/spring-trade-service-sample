package ru.sunsongs.auldanov.tradeservice.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author kraken
 * @since 6/2/16.
 */
@Entity
@Table(name = "sell_order")
public class SellOrder extends AbstractOrder {
}
