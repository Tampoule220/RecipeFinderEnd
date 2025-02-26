package com.example.recipefinder100;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private IngredientAdapter adapter;
    private List<Ingredient> ingredientList;
    private List<String> selectedIngredients;

    // Добавьте FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_view);
        ingredientList = new ArrayList<>();
        selectedIngredients = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new IngredientAdapter(ingredientList, selectedIngredients);
        recyclerView.setAdapter(adapter);

        loadIngredients();

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Проверка, авторизован ли пользователь
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Если пользователь не авторизован, перенаправляем его на экран входа
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Закрываем текущую активность
        } else {
            System.out.println("Пользователь авторизован: " + currentUser.getEmail());
        }
    }

    private void loadIngredients() {
        db.collection("ingredient")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ingredient ingredient = document.toObject(Ingredient.class);
                            ingredient.setId(document.getId());
                            ingredientList.add(ingredient);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void findRecipes(android.view.View view) {
        System.out.println("Кнопка 'Найти рецепты' нажата");
        System.out.println("Выбранные ингредиенты: " + selectedIngredients);

        if (selectedIngredients == null || selectedIngredients.isEmpty()) {
            System.out.println("Список выбранных ингредиентов пуст!");
            return;
        }

        Intent intent = new Intent(this, RecipeListActivity.class);
        intent.putStringArrayListExtra("selectedIngredients", new ArrayList<>(selectedIngredients));
        System.out.println("Выбранные ингредиенты: " + selectedIngredients);
        startActivity(intent);
    }
}