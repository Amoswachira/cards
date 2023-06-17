package com.cards.cards.service;

import com.cards.cards.dto.CardFilter;
import com.cards.cards.dto.UpdateCardRequest;
import com.cards.cards.model.Card;
import org.springframework.data.domain.Page;

public interface CardService {
    Card createCard( Card card);

    Page<Card> searchCards(String token, CardFilter filter, int page, int size);

    Card getCardById(Long cardId);

    Card updateCard( Long cardId, UpdateCardRequest cardRequest);

    void deleteCard( Long cardId);
}