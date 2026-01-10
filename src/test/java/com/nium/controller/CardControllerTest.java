package com.nium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nium.virtualcard.dto.request.CreateCardRequest;
import com.nium.virtualcard.dto.request.SpendRequest;
import com.nium.virtualcard.dto.request.TopUpRequest;
import com.nium.virtualcard.entity.Card;
import com.nium.virtualcard.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateCard() throws Exception {
        CreateCardRequest request = new CreateCardRequest();
        request.setCardholderName("Sourabh Gautam");
        request.setInitialBalance(BigDecimal.valueOf(200));

        Card card = new Card();
        card.setId(1L);
        card.setBalance(BigDecimal.valueOf(200));
        card.setCardholderName("Sourabh Gautam");

        when(cardService.createCard(any())).thenReturn(card);

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.balance").value(200));
    }

    @Test
    void shouldGetCard() throws Exception {
        Card card = new Card();
        card.setId(1L);
        card.setBalance(BigDecimal.valueOf(500));

        when(cardService.getCard(1L)).thenReturn(card);

        mockMvc.perform(get("/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.balance").value(500));
    }

    @Test
    void shouldTopUpCard() throws Exception {
        TopUpRequest request = new TopUpRequest();
        request.setAmount(BigDecimal.valueOf(800));

        Card card = new Card();
        card.setId(1L);
        card.setBalance(BigDecimal.valueOf(1000));

        when(cardService.topUp(eq(1L), any())).thenReturn(card);

        mockMvc.perform(post("/cards/1/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableBalance").value(1000));
    }

    @Test
    void shouldSpendMoney() throws Exception {
        SpendRequest request = new SpendRequest();
        request.setAmount(BigDecimal.valueOf(180));

        Card card = new Card();
        card.setId(1L);
        card.setBalance(BigDecimal.valueOf(820));

        when(cardService.spend(eq(1L), any())).thenReturn(card);

        mockMvc.perform(post("/cards/1/spend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableBalance").value(820));
    }

    @Test
    void shouldGetTransactions() throws Exception {
        when(cardService.getTransactions(1L)).thenReturn(List.of());

        mockMvc.perform(get("/cards/1/transactions"))
                .andExpect(status().isOk());
    }
}
