package ru.sunsongs.auldanov.tradeservice.dao.entity;

import ru.sunsongs.auldanov.tradeservice.model.Identifiable;

import javax.persistence.*;

/**
 * Base abstract class for all entities
 *
 * @author kraken
 */
@MappedSuperclass
public abstract class AbstractEntity implements Identifiable<Long> {
    private Long id;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof AbstractEntity)) return false;

        AbstractEntity that = (AbstractEntity) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}