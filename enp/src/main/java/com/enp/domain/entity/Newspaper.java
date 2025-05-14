package com.enp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class Newspaper {
    @Id
    Long id;
    @Column
    String title;
    @Column
    String summary;
    @Column
    String reporter;
    @Column
    String content;
    @Column
    Timestamp date;
}
