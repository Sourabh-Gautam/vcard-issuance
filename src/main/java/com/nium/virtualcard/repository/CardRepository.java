package com.nium.virtualcard.repository;

import com.nium.virtualcard.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Long, Card> {
}
