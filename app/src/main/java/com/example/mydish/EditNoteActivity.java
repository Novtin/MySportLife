package com.example.mydish;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

public class EditNoteActivity extends LowerPanel {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_save);
        ImageButton favoriteButton = findViewById(R.id.notes_button);
        favoriteButton.setImageResource(R.mipmap.chosen_notes);
    }
}
