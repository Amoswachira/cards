package com.cards.cards.model;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String color;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate creationDate;

    private String searchCards;

    public Card() {
        // Default constructor required by JPA
    }

    public Card(String name, String description, String color, CardStatus status, User user, LocalDate creationDate) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.status = status;
        this.user = user;
        this.creationDate = creationDate;
    }

    // Getters and setters

    public String getSearchCards() {
        return searchCards;
    }

    public void setSearchCards(String searchCards) {
        this.searchCards = searchCards;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
