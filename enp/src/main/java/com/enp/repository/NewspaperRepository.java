package com.enp.repository;

import com.enp.domain.entity.Newspaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewspaperRepository extends JpaRepository<Newspaper, Long> {
}
