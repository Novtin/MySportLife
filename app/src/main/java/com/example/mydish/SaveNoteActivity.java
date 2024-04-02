package com.example.mydish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.Sort;

public class SaveNoteActivity extends LowerPanel {
    private Realm realm;
    private EditText nameFoodText, proteinText, fatText, carbonText, kiloText, gramText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_save);
        ImageButton favoriteButton = findViewById(R.id.notes_button);
        favoriteButton.setImageResource(R.mipmap.chosen_notes);

        realm = Realm.getDefaultInstance();
        nameFoodText = findViewById(R.id.name_food_text);
        proteinText = findViewById(R.id.protein_text);
        fatText = findViewById(R.id.fat_text);
        carbonText = findViewById(R.id.carbon_text);
        kiloText = findViewById(R.id.kilo_text);
        gramText = findViewById(R.id.gram_text);
        String id = getIntent().getStringExtra("id");
        if (id != null){
            Note note = realm.where(Note.class)
                    .equalTo("id", Long.parseLong(id)).findFirst();
            if (note != null) {
                nameFoodText.setText(note.getNameFood());
                proteinText.setText(String.valueOf(note.getProtein()));
                fatText.setText(String.valueOf(note.getFat()));
                carbonText.setText(String.valueOf(note.getCarbon()));
                kiloText.setText(String.valueOf(note.getKilo()));
                gramText.setText(String.valueOf(note.getGram()));
            }
        }
    }

    public void saveNote(View view) {
        try {
            String nameFood = nameFoodText.getText().toString();
            double protein = Math.round(Double.parseDouble(proteinText.getText().toString()) * 100.0) / 100.0;
            double fat = Math.round(Double.parseDouble(fatText.getText().toString()) * 100.0) / 100.0;
            double carbon = Math.round(Double.parseDouble(carbonText.getText().toString()) * 100.0) / 100.0;
            double kilo = Math.round(Double.parseDouble(kiloText.getText().toString()) * 100.0) / 100.0;
            int gram = Integer.parseInt(gramText.getText().toString());
            if (protein > 100 || fat > 100 || carbon > 100 || kilo > 1000 || gram > 4000 || (protein + fat + carbon) > 100){
                throw new Exception();
            }
            String id = getIntent().getStringExtra("id");
            Note note;
            realm.beginTransaction();
            Note noteWithMaxId = realm.where(Note.class).sort("id", Sort.DESCENDING).findFirst();
            note = noteWithMaxId != null ? new Note(noteWithMaxId.getId() + 1) : new Note(1);
            if (id != null) {
                Note noteFromDB = realm.where(Note.class)
                        .equalTo("id", Long.parseLong(id)).findFirst();
                if (noteFromDB != null){
                    note = noteFromDB;
                }
            }
            note.setNameFood(nameFood);
            note.setProtein(protein);
            note.setFat(fat);
            note.setCarbon(carbon);
            note.setKilo(kilo);
            note.setGram(gram);
            realm.copyToRealmOrUpdate(note);
            realm.commitTransaction();
            Toast.makeText(this, "Заметка сохранена", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, NotesActivity.class);
            startActivity(intent);
        } catch (Exception exception){
            Toast.makeText(this, "Ошибка сохранения заметки", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, NotesActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void goBack(View view) {
        onBackPressed();
    }
}
