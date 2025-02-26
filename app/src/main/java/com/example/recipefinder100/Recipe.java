package com.example.recipefinder100;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {
    private String id; // ID документа в Firestore
    private String name;
    private String description;
    private String image_url; // Переименованное поле
    private List<String> ingredients;
    private String instructions;
    private List<String> missingIngredients; // Новое поле

    public Recipe() {}

    public Recipe(String id, String name, String description, String image_url, List<String> ingredients, String instructions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.missingIngredients = new ArrayList<>(); // Инициализация
    }

    // Геттеры и сеттеры для нового поля
    public List<String> getMissingIngredients() {
        return missingIngredients;
    }

    public void setMissingIngredients(List<String> missingIngredients) {
        this.missingIngredients = missingIngredients;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage_url() { return image_url; } // Переименованный геттер
    public void setImage_url(String image_url) { this.image_url = image_url; } // Переименованный сеттер

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}