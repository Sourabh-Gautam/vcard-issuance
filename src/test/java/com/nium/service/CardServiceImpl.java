package com.nium.service;

import com.nium.virtualcard.entity.Card;
import com.nium.virtualcard.entity.Transaction;
import com.nium.virtualcard.exception.InsufficientBalanceException;
import com.nium.virtualcard.repository.CardRepository;
import com.nium.virtualcard.repository.TransactionRepository;
import com.nium.virtualcard.service.impl.CardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void shouldTopUpCard() {
        Card card = new Card();
        card.setId(1L);
        card.setBalance(BigDecimal.valueOf(200));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Card updated = cardService.topUp(1L, BigDecimal.valueOf(300));

        assertEquals(BigDecimal.valueOf(500), updated.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldSpendMoney() {
        Card card = new Card();
        card.setId(1L);
        card.setBalance(BigDecimal.valueOf(500));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Card updated = cardService.spend(1L, BigDecimal.valueOf(200));

        assertEquals(BigDecimal.valueOf(300), updated.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldFailWhenInsufficientBalance() {
        Card card = new Card();
        card.setId(1L);
        card.setBalance(BigDecimal.valueOf(100));

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertThrows(InsufficientBalanceException.class,
                () -> cardService.spend(1L, BigDecimal.valueOf(500)));
    }
}
