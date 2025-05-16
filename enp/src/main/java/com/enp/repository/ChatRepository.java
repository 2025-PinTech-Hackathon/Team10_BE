package com.enp.repository;

import com.enp.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByUserId(Long userId);
}
