package com.enp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
