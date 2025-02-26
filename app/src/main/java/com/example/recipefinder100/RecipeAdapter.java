package com.example.recipefinder100;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
        System.out.println("Создан RecipeAdapter с " + recipes.size() + " рецептами.");
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        // Загружаем изображение
        String imageUrl = recipe.getImage_url();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground); // Заглушка
        }

        // Устанавливаем название рецепта
        holder.textView.setText(recipe.getName());

        // Отображаем недостающие ингредиенты
        List<String> missingIngredients = recipe.getMissingIngredients();
        if (missingIngredients != null && !missingIngredients.isEmpty()) {
            holder.missingIngredients.setText("Не хватает: " + TextUtils.join(", ", missingIngredients));
            holder.missingIngredients.setVisibility(View.VISIBLE);
        } else {
            holder.missingIngredients.setVisibility(View.GONE);
        }

        // Обработка клика
        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
    }

    @Override
    public int getItemCount() {
        int count = recipes.size();
        System.out.println("Количество рецептов в адаптере: " + count);
        return count;
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView missingIngredients;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipe_image);
            textView = itemView.findViewById(R.id.recipe_name);
            missingIngredients = itemView.findViewById(R.id.missing_ingredients);
        }
    }
}