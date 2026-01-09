package com.nium.virtualcard.controller;

import com.nium.virtualcard.dto.request.CreateCardRequest;
import com.nium.virtualcard.dto.request.SpendRequest;
import com.nium.virtualcard.dto.response.CardResponse;
import com.nium.virtualcard.dto.response.RemainingBalanceResponse;
import com.nium.virtualcard.entity.Card;
import com.nium.virtualcard.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
@Tag(name = "Cards", description = "Virtual Card Issuance and Spending APIs")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Create a new virtual card")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponse createCard(@Valid @RequestBody CreateCardRequest request) {
        Card card = cardService.createCard(request);
        return new CardResponse(card);
    }

    @Operation(summary = "Spend amount from a virtual card")
    @PostMapping("/{id}/spend")
    @ResponseStatus(HttpStatus.OK)
    public RemainingBalanceResponse spend(
            @PathVariable Long id,
            @Valid @RequestBody SpendRequest request) {
        Card updatedCard = cardService.spend(id, request.getAmount());
        return new RemainingBalanceResponse(updatedCard.getId(), updatedCard.getBalance());
    }

    @Operation(summary = "Get virtual card details")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CardResponse getCard(@PathVariable Long id) {
        return new CardResponse(cardService.getCard(id));
    }

}

