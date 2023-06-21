package com.eugenescobich.outbox.pattern.persistance.repository;

import static org.hibernate.LockMode.UPGRADE_SKIPLOCKED;

import com.eugenescobich.outbox.pattern.persistance.jpa.ExtendedJpaRepository;
import com.eugenescobich.outbox.pattern.persistance.model.Task;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.jpa.SpecHints;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends ExtendedJpaRepository<Task, Long> {

    @QueryHints(@QueryHint(name = AvailableSettings.JAKARTA_LOCK_TIMEOUT, value = "-2"))
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Task as t where t.nextRetryTime <= :time and t.numberOfRetries <= :maxNumberOfRetries and t.sent = false")
    List<Task> findTask(Pageable pageable, LocalDateTime time, Integer maxNumberOfRetries);

    @QueryHints(@QueryHint(name = AvailableSettings.JAKARTA_LOCK_TIMEOUT, value = "0"))
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Task as t where t.entityId = :entityId and t.sent = false order by t.createdAt")
    List<Task> findOrderedTask(PageRequest pageable, Long entityId);

    @Query("SELECT t FROM Task as t where MOD(t.entityId, :divideBy) = :restOfDivision and t.sent = false order by t.createdAt")
    List<Task> findTaskByDivision(Pageable pageable, Integer divideBy, Integer restOfDivision);

    @Modifying
    @Query("UPDATE Task as t SET t.sent = true where t.id = :taskId")
    void saveByUpdate(Long taskId);
}
