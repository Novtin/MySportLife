package com.example.mydish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ChosenRecipeActivity extends LowerPanel {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_chosen);
        ImageButton favoriteButton = findViewById(R.id.recipes_button);
        favoriteButton.setImageResource(R.mipmap.chosen_recipes);
    }

    public void goToRecipe(View view) {
        Intent intent = new Intent(this, ChosenRecipeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void goBack(View view) {
        onBackPressed();
    }

}
