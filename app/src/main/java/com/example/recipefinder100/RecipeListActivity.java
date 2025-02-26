package com.example.recipefinder100;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
        recipeList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        System.out.println("RecipeListActivity создан");

        List<String> selectedIngredients = getIntent().getStringArrayListExtra("selectedIngredients");
        System.out.println("Полученные ингредиенты из Intent: " + selectedIngredients);
        if (selectedIngredients == null || selectedIngredients.isEmpty()) {
            System.out.println("Список выбранных ингредиентов пуст или не передан!");
            return;
        }

        System.out.println("Полученные ингредиенты: " + selectedIngredients);

        adapter = new RecipeAdapter(recipeList, this::openRecipeDetails);
        recyclerView.setAdapter(adapter);

        loadRecipes(selectedIngredients);
    }

    private void loadRecipes(List<String> selectedIngredients) {
        db.collection("recipes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        recipeList.clear(); // Очищаем старые данные

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Recipe recipe = document.toObject(Recipe.class);
                            recipe.setId(document.getId());

                            List<String> recipeIngredients = recipe.getIngredients();
                            List<String> missingIngredients = new ArrayList<>();

                            // Вычисляем недостающие ингредиенты
                            for (String ingredient : recipeIngredients) {
                                if (!selectedIngredients.contains(ingredient)) {
                                    missingIngredients.add(ingredient);
                                }
                            }

                            // Проверяем условия отображения рецепта
                            int missingCount = missingIngredients.size();
                            if (missingCount <= 2) { // Показываем, если не хватает 0, 1 или 2 ингредиента
                                recipe.setMissingIngredients(missingIngredients); // Добавляем недостающие ингредиенты в рецепт
                                recipeList.add(recipe);
                            }
                        }

                        adapter.notifyDataSetChanged(); // Обновляем адаптер
                    } else {
                        System.err.println("Ошибка загрузки рецептов: " + task.getException());
                    }
                });
    }

    private void openRecipeDetails(Recipe recipe) {
        System.out.println("Открытие деталей рецепта: " + recipe.getName());

        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }
}