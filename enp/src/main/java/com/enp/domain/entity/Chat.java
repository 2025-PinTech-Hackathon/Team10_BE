package com.enp.domain.entity;

import jakarta.persistence.*;
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
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 매핑
    User user;
}
