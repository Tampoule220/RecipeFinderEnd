package com.example.recipefinder100;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Инициализация кнопок навигации
        View homeButton = findViewById(R.id.imageView_home);
        View searchButton = findViewById(R.id.imageView_search);
        View profileButton = findViewById(R.id.imageView_profile);

        // Обработчик для кнопки "Главная"
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Обработчик для кнопки "Поиск"
        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        // Обработчик для кнопки "Профиль"
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.search_results);
        EditText searchInput = findViewById(R.id.search_input);

        recipeList = new ArrayList<>();
        adapter = new SearchAdapter(recipeList, this::openRecipeDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Добавляем слушатель на изменение текста в поисковой строке
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    searchRecipes(query);
                } else {
                    recipeList.clear();
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchRecipes(String query) {
        String lowerCaseQuery = query.toLowerCase().trim();

        db.collection("recipes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        recipeList.clear(); // Очищаем старые данные
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Recipe recipe = document.toObject(Recipe.class);
                            recipe.setId(document.getId());

                            String recipeName = recipe.getName().toLowerCase();
                            if (recipeName.contains(lowerCaseQuery)) {
                                recipeList.add(recipe);
                                System.out.println("Найден рецепт: " + recipe.getName());
                            }
                        }
                        adapter.notifyDataSetChanged(); // Обновляем адаптер
                        recyclerView.setVisibility(View.VISIBLE); // Показываем RecyclerView
                    } else {
                        System.err.println("Ошибка поиска рецептов: " + task.getException());
                    }
                });
    }

    private void openRecipeDetails(Recipe recipe) {
        // Создаем Intent для перехода на RecipeDetailsActivity
        Intent intent = new Intent(this, RecipeDetailsActivity.class);

        // Передаем объект Recipe через Intent
        intent.putExtra("recipe", recipe);

        // Запускаем новую активность
        startActivity(intent);
    }
}