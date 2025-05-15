package com.enp.repository;

import com.enp.domain.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query(value = "SELECT * FROM Quiz ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Quiz> findRandomQuiz();
}
