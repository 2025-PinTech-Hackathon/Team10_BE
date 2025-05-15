package com.enp.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
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
    @Column
    Long readCount;
    @Column
    Integer textSize;
    @Column
    Integer lineGap;
    @OneToMany(mappedBy = "User", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats;
}
