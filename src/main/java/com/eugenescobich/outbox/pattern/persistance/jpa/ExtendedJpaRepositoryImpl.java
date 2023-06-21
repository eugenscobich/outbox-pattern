package com.eugenescobich.outbox.pattern.persistance.jpa;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

@Slf4j
public class ExtendedJpaRepositoryImpl<T, I extends Serializable> extends SimpleJpaRepository<T, I>
    implements ExtendedJpaRepository<T, I> {

    private final EntityManager entityManager;

    /**
     * Creates a new {@link ExtendedJpaRepositoryImpl} to manage objects of the given JpaEntityInformation.
     *
     * @param entityInformation the entity info
     * @param entityManager     the entity manager
     */
    public ExtendedJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
