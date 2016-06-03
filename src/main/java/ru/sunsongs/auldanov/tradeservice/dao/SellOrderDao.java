package ru.sunsongs.auldanov.tradeservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sunsongs.auldanov.tradeservice.dao.entity.SellOrder;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author kraken
 * @since 6/2/16.
 */
public interface SellOrderDao extends JpaRepository<SellOrder, Long> {
    List<SellOrder> findByRemainGreaterThan(long remain);

    @Query("SELECT so FROM SellOrder so WHERE so.price <= ?1 ORDER BY so.id ASC")
    List<SellOrder> getSuitableOrders(@Nonnull BigDecimal price);
}
