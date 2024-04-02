package com.example.mydish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class FavoriteNewsActivity extends LowerPanel {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.favorites_news);
        //ImageButton favoriteButton = findViewById(R.id.favorites_button);
        //favoriteButton.setImageResource(R.mipmap.chosen_favorites);
    }
    private boolean isFavorite = false;
    public void makeUnmakeFavorite(View view) {
        ImageButton favoriteButton = (ImageButton) view;
        if (isFavorite) {
            favoriteButton.setImageResource(R.mipmap.favorites_mini);
        } else {
            favoriteButton.setImageResource(R.mipmap.made_favorite);
        }
        isFavorite = !isFavorite;
    }

    public void goToNew(View view) {
        Intent intent = new Intent(this, ChosenNewActivity.class);
        startActivity(intent);
    }
}
