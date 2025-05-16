package com.enp.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String nickname;
    @Column(nullable = false)
    String loginId;
    @Column(nullable = false)
    String password;
    @Column
    Integer todayQuizCount;
    @Column
    Long point;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats;
}
