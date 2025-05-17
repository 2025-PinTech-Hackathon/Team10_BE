package com.enp.repository;

import com.enp.domain.entity.Chat;
import com.enp.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE c.user = :user")
    List<Chat> findAllByUser(User user);
}
