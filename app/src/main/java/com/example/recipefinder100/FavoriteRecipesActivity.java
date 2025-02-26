package com.example.recipefinder100;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class FavoriteRecipesActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> favoriteRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_recipes);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.favorite_recycler_view);
        favoriteRecipes = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Передаем обработчик кликов в адаптер
        adapter = new RecipeAdapter(favoriteRecipes, this::openRecipeDetails);
        recyclerView.setAdapter(adapter);

        loadFavoriteRecipes();
    }

    private void loadFavoriteRecipes() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users")
                .document(userId)
                .collection("favorites")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        favoriteRecipes.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Recipe recipe = document.toObject(Recipe.class);
                            favoriteRecipes.add(recipe);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    // Метод для открытия деталей рецепта
    private void openRecipeDetails(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipe", recipe); // Передаем объект Recipe
        startActivity(intent);
    }
}