package com.cards.cards.controller;

import com.cards.cards.dto.CardFilter;
import com.cards.cards.dto.CreateCardRequest;
import com.cards.cards.dto.UpdateCardRequest;
import com.cards.cards.model.Card;
import com.cards.cards.model.CardStatus;
import com.cards.cards.model.User;
import com.cards.cards.service.CardService;
import com.cards.cards.service.UserService;
import com.cards.cards.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/cards")
public class CardController {
    private CardService cardService;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    @Autowired
    public CardController(CardService cardService, JwtTokenProvider jwtTokenProvider,UserService userService) {
        this.cardService = cardService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Card> createCard(
            HttpServletRequest request,
            @Valid @RequestBody CreateCardRequest cardRequest
    ) {

        User user = userService.getUserFromToken(jwtTokenProvider.extractToken(request));
        Card card = new Card();
        card.setName(cardRequest.getName());
        card.setDescription(cardRequest.getDescription());
        card.setColor(cardRequest.getColor());
        card.setUser(user);

        Card createdCard = cardService.createCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    @GetMapping
    public ResponseEntity<Page<Card>> searchCards(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) CardStatus status,
            @RequestParam(required = false) LocalDate creationDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        String token = jwtTokenProvider.extractToken(request);
        CardFilter filter = new CardFilter(name, color, status, creationDate);
        Page<Card> cardPage = cardService.searchCards(token, filter, page, size);
        return ResponseEntity.ok(cardPage);
    }
    @GetMapping("/{cardId}")
    public ResponseEntity<Card> getCardById(
            @PathVariable Long cardId
    ) {

        Card card = cardService.getCardById( cardId);
        return ResponseEntity.ok(card);
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<Card> updateCard(
            @PathVariable Long cardId,
            @Valid @RequestBody UpdateCardRequest cardRequest
    ) {

        Card updatedCard = cardService.updateCard(cardId, cardRequest);
        return ResponseEntity.ok(updatedCard);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable Long cardId
    ) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}
