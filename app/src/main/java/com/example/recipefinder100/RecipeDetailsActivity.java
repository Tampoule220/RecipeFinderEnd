package com.example.recipefinder100;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class RecipeDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ImageView favoriteIcon;
    private Recipe recipe;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // Инициализация Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Получаем рецепт из Intent
        recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        if (recipe == null) {
            finish();
            return;
        }

        // Привязка элементов интерфейса
        ImageView recipeImage = findViewById(R.id.recipe_image);
        TextView recipeName = findViewById(R.id.recipe_name);
        TextView recipeDescription = findViewById(R.id.recipe_description);
        TextView recipeIngredients = findViewById(R.id.recipe_ingredients);
        TextView recipeInstructions = findViewById(R.id.recipe_instructions);
        favoriteIcon = findViewById(R.id.imageView3);

        // Устанавливаем данные в элементы интерфейса
        Picasso.get().load(recipe.getImage_url()).into(recipeImage);
        recipeName.setText(recipe.getName());
        recipeDescription.setText(recipe.getDescription());
        recipeIngredients.setText(TextUtils.join(", ", recipe.getIngredients()));
        recipeInstructions.setText(recipe.getInstructions());

        // Проверяем, является ли рецепт избранным
        checkIfFavorite();
    }

    private void checkIfFavorite() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users")
                .document(userId)
                .collection("favorites")
                .document(recipe.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                        isFavorite = true;
                        favoriteIcon.setImageResource(android.R.drawable.btn_star_big_on);
                    } else {
                        isFavorite = false;
                        favoriteIcon.setImageResource(android.R.drawable.btn_star_big_off);
                    }
                });
    }

    public void toggleFavorite(View view) {
        String userId = mAuth.getCurrentUser().getUid();
        if (isFavorite) {
            // Удаляем рецепт из избранных
            db.collection("users")
                    .document(userId)
                    .collection("favorites")
                    .document(recipe.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        isFavorite = false;
                        favoriteIcon.setImageResource(android.R.drawable.btn_star_big_off);
                        Toast.makeText(this, "Рецепт удален из избранных", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Добавляем рецепт в избранные
            db.collection("users")
                    .document(userId)
                    .collection("favorites")
                    .document(recipe.getId())
                    .set(recipe)
                    .addOnSuccessListener(aVoid -> {
                        isFavorite = true;
                        favoriteIcon.setImageResource(android.R.drawable.btn_star_big_on);
                        Toast.makeText(this, "Рецепт добавлен в избранные", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}