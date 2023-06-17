package com.cards.cards.service;

import com.cards.cards.dto.CardFilter;
import com.cards.cards.dto.UpdateCardRequest;
import com.cards.cards.exception.NotFoundException;
import com.cards.cards.model.Card;
import com.cards.cards.model.CardStatus;
import com.cards.cards.model.User;
import com.cards.cards.repository.CardRepository;
import com.cards.cards.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, JwtTokenProvider jwtTokenProvider,UserService userService) {
        this.cardRepository = cardRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public Card createCard(Card card) {
        card.setStatus(CardStatus.TODO);
        return cardRepository.save(card);
    }

    @Override
    public Page<Card> searchCards(String token, CardFilter filter, int page, int size) {
        User user = userService.getUserFromToken(token);
        Pageable pageable = PageRequest.of(page, size);

        return cardRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null) {
                predicates.add(builder.like(builder.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }

            if (filter.getColor() != null) {
                predicates.add(builder.equal(root.get("color"), filter.getColor()));
            }

            if (filter.getStatus() != null) {
                predicates.add(builder.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getCreationDate() != null) {
                predicates.add(builder.equal(root.get("creationDate"), filter.getCreationDate()));
            }

            predicates.add(builder.equal(root.get("user"), user));

            return builder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Override
    public Card getCardById( Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card not found"));
    }

    @Override
    public Card updateCard( Long cardId, UpdateCardRequest cardRequest) {
        Card card = getCardById(cardId);

        card.setName(cardRequest.getName());
        card.setDescription(cardRequest.getDescription());
        card.setColor(cardRequest.getColor());
        card.setStatus(cardRequest.getStatus());

        return cardRepository.save(card);
    }

    @Override
    public void deleteCard( Long cardId) {
        Card card = getCardById(cardId);
        cardRepository.delete(card);
    }

    private User getCurrentUserFromToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = jwtTokenProvider.extractToken(request);
        String email = jwtTokenProvider.getEmailFromToken(token);
        // Here, retrieve the user object using the email
        // You can use your existing logic or your User repository to fetch the user object
        // Return the user object
        return null;
    }
}
