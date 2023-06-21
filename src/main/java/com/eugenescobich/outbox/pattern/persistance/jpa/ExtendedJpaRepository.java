package com.eugenescobich.outbox.pattern.persistance.jpa;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;


@NoRepositoryBean
public interface ExtendedJpaRepository<T, I extends Serializable> extends JpaRepository<T, I> {

    EntityManager getEntityManager();

}
