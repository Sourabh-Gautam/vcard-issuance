package com.nium.virtualcard.repository;

import com.nium.virtualcard.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardIdOrderByCreatedAtDesc(Long cardId);
}