package com.enp.domain.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Chat {
    @Id
    Long id;
    @Column
    String content;
    @Column
    Boolean isAI;
    @Column
    Timestamp date;
    @ManyToOne
    User user;
}
