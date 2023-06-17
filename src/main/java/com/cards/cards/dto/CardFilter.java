package com.cards.cards.dto;

import com.cards.cards.model.CardStatus;

import java.time.LocalDate;

public class CardFilter {
    private String name;
    private String color;
    private CardStatus status;
    private LocalDate creationDate;

    public CardFilter(String name, String color, CardStatus status, LocalDate creationDate) {
        this.name = name;
        this.color = color;
        this.status = status;
        this.creationDate = creationDate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}

