package ru.sunsongs.auldanov.tradeservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sunsongs.auldanov.tradeservice.dao.entity.BuyOrder;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author kraken
 * @since 6/2/16.
 */
public interface BuyOrderDao extends JpaRepository<BuyOrder, Long> {
    List<BuyOrder> findByRemainGreaterThan(long remain);

    @Query("SELECT bo FROM BuyOrder bo WHERE bo.price >= ?1 ORDER BY bo.id ASC")
    List<BuyOrder> getSuitableOrders(@Nonnull BigDecimal price);
}
