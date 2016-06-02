package ru.sunsongs.auldanov.tradeservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sunsongs.auldanov.tradeservice.dao.entity.BuyOrder;

import java.util.List;

/**
 * @author kraken
 * @since 6/2/16.
 */
public interface BuyOrderDao extends JpaRepository<BuyOrder, Long> {
    List<BuyOrder> findByReminderGreaterThan0();
}
