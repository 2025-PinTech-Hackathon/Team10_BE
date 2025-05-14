package com.enp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Quiz {
    @Id
    Long id;
    @Column
    String content;
    @Column
    String answer;
}
