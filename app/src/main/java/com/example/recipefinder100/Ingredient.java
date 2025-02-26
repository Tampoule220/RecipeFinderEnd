package com.example.recipefinder100;

public class Ingredient {
    private String id; // ID документа в Firestore
    private String name;
    private String image_url; // Переименованная переменная

    public Ingredient() {}

    public Ingredient(String id, String name, String image_url) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImage_url() { return image_url; } // Переименованный геттер
    public void setImage_url(String image_url) { this.image_url = image_url; } // Переименованный сеттер
}