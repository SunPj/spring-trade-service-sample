package ru.sunsongs.auldanov.tradeservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sunsongs.auldanov.tradeservice.dao.entity.SellOrder;

import java.util.List;

/**
 * @author kraken
 * @since 6/2/16.
 */
public interface SellOrderDao extends JpaRepository<SellOrderDao, Long> {
    List<SellOrder> findByReminderGreaterThan0();
}
