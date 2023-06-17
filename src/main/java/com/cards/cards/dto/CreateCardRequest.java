package com.cards.cards.dto;

import javax.validation.constraints.NotBlank;

public class CreateCardRequest {
    @NotBlank(message = "Card name is required")
    private String name;

    private String description;

    private String color;

    public CreateCardRequest(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
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
}
