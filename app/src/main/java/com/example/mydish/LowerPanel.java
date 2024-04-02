package com.example.mydish;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LowerPanel extends AppCompatActivity {
    public void goToNews(View view) {
        Intent intent = new Intent(this, NewsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goToNotes(View view) {
        Intent intent = new Intent(this, NotesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void goToRecipes(View view) {
        Intent intent = new Intent(this, SportActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    //public void goToFavoritesNews(View view) {
    //    Intent intent = new Intent(this, FavoriteNewsActivity.class);
    //    startActivity(intent);
    //}
    //public void goToFavoritesRecipes(View view) {
    //    Intent intent = new Intent(this, FavoriteRecipesActivity.class);
    //    startActivity(intent);
    //}
}
