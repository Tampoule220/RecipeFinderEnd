package com.example.recipefinder100;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private List<Ingredient> ingredients; // Указываем тип данных
    private List<String> selectedIngredients; // Указываем тип данных

    public IngredientAdapter(List<Ingredient> ingredients, List<String> selectedIngredients) {
        this.ingredients = ingredients;
        this.selectedIngredients = selectedIngredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Убедитесь, что файл item_ingredient.xml существует в папке res/layout/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        // Загружаем изображение с помощью Picasso
        Picasso.get().load(ingredient.getImage_url()).into(holder.imageView);

        // Устанавливаем название ингредиента
        holder.textView.setText(ingredient.getName());

        // Устанавливаем цвет фона в зависимости от состояния выбора
        if (selectedIngredients.contains(ingredient.getName())) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        // Обработка клика на элемент
        holder.itemView.setOnClickListener(v -> {
            if (selectedIngredients.contains(ingredient.getName())) {
                selectedIngredients.remove(ingredient.getName());
            } else {
                selectedIngredients.add(ingredient.getName());
            }

            // Уведомляем адаптер о необходимости обновить этот элемент
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            // Убедитесь, что ID в XML соответствуют этим именам
            imageView = itemView.findViewById(R.id.ingredient_image);
            textView = itemView.findViewById(R.id.ingredient_name);
        }
    }
}