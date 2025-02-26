package com.example.recipefinder100;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // Код для выбора изображения
    private ImageView avatarImage;
    private TextView userName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Инициализация Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
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

        // Привязка элементов интерфейса
        avatarImage = findViewById(R.id.avatar_image);
        userName = findViewById(R.id.user_name);

        if (currentUser != null) {
            // Установка имени пользователя
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                userName.setText(displayName);
            } else {
                userName.setText("Пользователь");
            }

            // Установка аватарки (если есть)
            Uri photoUrl = currentUser.getPhotoUrl();
            if (photoUrl != null) {
                Picasso.get().load(photoUrl).into(avatarImage);
            }
        }

        // Обработчик клика на аватарку
        avatarImage.setOnClickListener(v -> changeAvatar());

        // Обработчик клика на имя пользователя
        userName.setOnClickListener(v -> changeUserName());
    }

    // Метод для открытия галереи при нажатии на аватарку
    private void changeAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Метод для изменения имени пользователя
    private void changeUserName() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_text, null);
        EditText editText = dialogView.findViewById(R.id.edit_text);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Изменить имя пользователя")
                .setView(dialogView)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String newDisplayName = editText.getText().toString().trim();

                    if (newDisplayName.isEmpty()) {
                        Toast.makeText(this, "Имя не может быть пустым", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(newDisplayName)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        userName.setText(newDisplayName);
                                        Toast.makeText(ProfileActivity.this, "Имя успешно изменено", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ProfileActivity.this, "Ошибка при изменении имени", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    // Обработка результата выбора изображения из галереи
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            // Загружаем выбранное изображение в ImageView
            Picasso.get().load(selectedImageUri).into(avatarImage);

            // Здесь можно добавить логику загрузки изображения в Firebase Storage
            Toast.makeText(this, "Аватарка изменена", Toast.LENGTH_SHORT).show();
        }
    }

    // Обработчик для открытия избранных рецептов
    public void openFavoriteRecipes(View view) {
        Intent intent = new Intent(this, FavoriteRecipesActivity.class);
        startActivity(intent);
    }

    // Обработчик для открытия настроек
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // Обработчик для выхода из аккаунта
    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}