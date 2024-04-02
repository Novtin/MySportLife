package com.example.mydish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ChosenNewActivity extends LowerPanel {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_chosen);
        ImageButton favoriteButton = findViewById(R.id.news_button);
        favoriteButton.setImageResource(R.mipmap.chosen_news);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void goBack(View view) {
        onBackPressed();
    }
}
