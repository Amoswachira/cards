package com.cards.cards.repository;

import com.cards.cards.model.Card;
import com.cards.cards.model.CardStatus;
import com.cards.cards.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    @Query("SELECT c FROM Card c WHERE (:name IS NULL OR c.name = :name) " +
            "AND (:color IS NULL OR c.color = :color) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:creationDate IS NULL OR c.creationDate = :creationDate) " +
            "AND c.user = :user")
    Page<Card> findByFilterAndUser(
            @Param("name") String name,
            @Param("color") String color,
            @Param("status") CardStatus status,
            @Param("creationDate") LocalDate creationDate,
            @Param("user") User user,
            Pageable pageable
    );
}
