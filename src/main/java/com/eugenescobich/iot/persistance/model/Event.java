package com.eugenescobich.iot.persistance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_event")
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_event_seq_generator")
    @SequenceGenerator(name = "t_event_seq_generator", sequenceName = "t_event_seq", allocationSize = 10, initialValue = 10)
    private Long id;

    @Column
    private String name;
}
