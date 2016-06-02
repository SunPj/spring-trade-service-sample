package ru.sunsongs.auldanov.tradeservice.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.sunsongs.auldanov.tradeservice.dao.entity.Execution;

import java.util.List;
import java.util.Optional;

/**
 * @author kraken
 * @since 6/2/16.
 */
public interface ExecutedOrderDao extends JpaRepository<Execution, Long> {
    List<Execution> findOrderByIdDesc(Pageable pageable);

    Optional<Execution> findTop1OrderByIdDesc();
}
