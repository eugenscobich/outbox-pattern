package com.eugenescobich.outbox.pattern.persistance.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_task")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_task_seq_generator")
    @SequenceGenerator(name = "t_task_seq_generator", sequenceName = "t_task_seq", allocationSize = 10, initialValue = 10)
    private Long id;

    @Column
    private Long entityId;

    @Column
    private LocalDateTime nextRetryTime;

    @Column
    private Integer numberOfRetries;

    @Column
    private LocalDateTime createdAt;

    private Boolean sent;
}
