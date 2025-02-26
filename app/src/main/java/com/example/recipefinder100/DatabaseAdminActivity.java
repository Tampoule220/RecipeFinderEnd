package com.example.recipefinder100;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseAdminActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация Firestore
        db = FirebaseFirestore.getInstance();
        // Добавление ингредиентов
        addIngredients();
        // Добавление рецептов
        addRecipes();
    }

    private void addIngredients() {
        // Пример списка ингредиентов
        List<Map<String, Object>> ingredients = Arrays.asList(
                createIngredient("Сыр творожный", "https://i.imgur.com/ltFVHTg.jpg"),
                createIngredient("Яйца", "https://i.imgur.com/Oc3AkvS.jpg"),
                createIngredient("Крупа манная", "https://i.imgur.com/5wQ8YJ5.jpg"),
                createIngredient("Масло сливочное", "https://i.imgur.com/9fidpez.jpg")
        );

        // Добавление ингредиентов в коллекцию "ingredient"
        for (Map<String, Object> ingredient : ingredients) {
            String customId = ((String) ingredient.get("name")).toLowerCase().replace(" ", "_"); // Создаем ID на основе имени
            db.collection("ingredient").document(customId).set(ingredient)
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Ингредиент успешно добавлен с ID: " + customId);
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Ошибка при добавлении ингредиента: " + e.getMessage());
                    });
        }
    }

    private void addRecipes() {
        // Пример рецепта
        Map<String, Object> recipe = new HashMap<>();
        recipe.put("name", "Творожная запеканка");
        recipe.put("description", "Запеканка из творожного сливочного сыра, с манной крупой, без муки, получается однородной, очень нежной и в меру сладкой.");
        recipe.put("image_url", "https://i.imgur.com/9Yhzz03.jpg");
        recipe.put("ingredients", Arrays.asList("Сыр творожный", "Яйца", "Крупа манная", "Масло сливочное"));
        recipe.put("instructions", "Подготавливаем продукты. В глубокой миске соединяем творожный сыр и яйца. Перетираем погружным блендером до однородности. Всыпаем сахар, ещё раз перетираем массу. Добавляем манку и перемешиваем. Оставляем миску с творожной массой на 20 минут. Включаем духовку для разогрева до 180 градусов. Форму для выпечки смазываем сливочным маслом. Затем слегка присыпаем манкой дно и борта. Заполняем форму творожной массой и отправляем в разогретую духовку на 30-35 минут при температуре 180 градусов. Испечённую запеканку полностью остужаем. Быстрая творожная запеканка готова. Разрезаем на порционные кусочки и подаём к столу. Приятного аппетита!");

        // Добавление рецепта в коллекцию "recipes" с пользовательским ID
        String customRecipeId = "Творожная_запеканка"; // Пример пользовательского ID
        db.collection("recipes").document(customRecipeId).set(recipe)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Рецепт успешно добавлен с ID: " + customRecipeId);
                })
                .addOnFailureListener(e -> {
                    System.err.println("Ошибка при добавлении рецепта: " + e.getMessage());
                });
    }

    private Map<String, Object> createIngredient(String name, String imageUrl) {
        Map<String, Object> ingredient = new HashMap<>();
        ingredient.put("name", name);
        ingredient.put("image_url", imageUrl);
        return ingredient;
    }
}