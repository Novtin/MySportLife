package com.example.mydish;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class NotesActivity extends LowerPanel {
    private Realm realm;
    private LinearLayout foodLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);
        ImageButton favoriteButton = findViewById(R.id.notes_button);
        favoriteButton.setImageResource(R.mipmap.chosen_notes);
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        foodLayout = findViewById(R.id.miniFood);
        displayDataFromRealm();
    }
    private void displayDataFromRealm() {
        foodLayout.removeAllViews();
        TreeMap<String, List<Note>> dateWithNote = new TreeMap<>(Comparator.reverseOrder());
        dateWithNote.putAll(getDataGroupedByDateAndSortedById());
        if (!dateWithNote.isEmpty()) {
            for (String date : dateWithNote.keySet()) {
                makeData(date);
                for (Note note : Objects.requireNonNull(dateWithNote.get(date))) {
                        makeFood(note);
                    }
                makeSummary(Objects.requireNonNull(dateWithNote.get(date)));
            }
        }
    }

    public void goToSaveNotes(View view) {
        Intent intent = new Intent(this, SaveNoteActivity.class);
        startActivity(intent);
    }

    public void goToEditNotes(View view) {
        Intent intent = new Intent(this, SaveNoteActivity.class);
        intent.putExtra("id", String.valueOf((long) view.getTag()));
        startActivity(intent);
    }

    public void deleteNote(View view) {
        long idForDeletion = (long) view.getTag();
        realm.beginTransaction();
        Note note = realm.where(Note.class).equalTo("id", idForDeletion).findFirst();
        if (note != null) {
            note.deleteFromRealm();
        }
        realm.commitTransaction();
        Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void makeData(String data) {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);

        View divider = new View(this);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                0,
                2,
                1
        );
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(Color.BLACK);
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(textParams);
        textView.setText(data);
        textView.setTextSize(25);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(null, Typeface.ITALIC);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);

        linearLayout.addView(divider);
        linearLayout.addView(textView);
        View divider1 = new View(this);
        divider1.setLayoutParams(dividerParams);
        divider1.setBackgroundColor(Color.BLACK);
        linearLayout.addView(divider1);

        foodLayout.addView(linearLayout);
    }

    private void makeFood(Note note){
    LinearLayout linearLayout = new LinearLayout(this);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );
    params.setMargins(20, 20, 20, 20);
    linearLayout.setLayoutParams(params);
    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    linearLayout.setGravity(Gravity.CENTER);
    linearLayout.setBackgroundResource(R.drawable.stroke_corners);

    TextView textView = new TextView(this);
    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );
    textParams.setMargins(30, 20, 20, 20);
    textView.setLayoutParams(textParams);
    textView.setText(note.getNameFood());
    textView.setTextSize(25);
    textView.setTypeface(Typeface.create("@font/inter", Typeface.BOLD_ITALIC));
    textView.setTextColor(Color.BLACK);
    textView.setGravity(Gravity.CENTER_HORIZONTAL);
    linearLayout.addView(textView);

    LinearLayout tableLayout = new LinearLayout(this);
    LinearLayout.LayoutParams tableParams = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
    );
    tableParams.setMargins(10, 10, 10, 10);
    tableLayout.setLayoutParams(tableParams);
    tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);
    tableLayout.setOrientation(LinearLayout.VERTICAL);

    LinearLayout textRow = new LinearLayout(this);
    textRow.setOrientation(LinearLayout.HORIZONTAL);
    TextView text = new TextView(this);
    text.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
    ));
    text.setText(String.valueOf(Math.round(note.getProtein() * note.getGram() / 100 * 100.0) / 100.0));
    text.setTextSize(18);
    text.setTypeface(Typeface.create("@font/inter", Typeface.ITALIC));
    text.setTextColor(Color.BLACK);
    text.setGravity(Gravity.CENTER);
    textRow.addView(text);
    TextView text1 = new TextView(this);
    text1.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
    ));
    text1.setTextSize(18);
    text1.setTypeface(Typeface.create("@font/inter", Typeface.ITALIC));
    text1.setTextColor(Color.BLACK);
    text1.setGravity(Gravity.CENTER);
    text1.setText(String.valueOf(Math.round(note.getFat() * note.getGram() / 100 * 100.0) / 100.0));
    textRow.addView(text1);
    TextView text2 = new TextView(this);
    text2.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
    ));
    text2.setTextSize(18);
    text2.setTypeface(Typeface.create("@font/inter", Typeface.ITALIC));
    text2.setTextColor(Color.BLACK);
    text2.setGravity(Gravity.CENTER);
    text2.setText(String.valueOf(Math.round(note.getCarbon() * note.getGram() / 100 * 100.0) / 100.0));
    textRow.addView(text2);
    tableLayout.addView(textRow);

    LinearLayout rowUnder = new LinearLayout(this);
    rowUnder.setOrientation(LinearLayout.HORIZONTAL);
    TextView miniText = new TextView(this);
    miniText.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
    ));
    miniText.setText(R.string.b);
    miniText.setTextColor(Color.BLACK);
    miniText.setGravity(Gravity.CENTER);

    rowUnder.addView(miniText);
    TextView miniText1 = new TextView(this);
    miniText1.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
    ));
    miniText1.setTextColor(Color.BLACK);
    miniText1.setGravity(Gravity.CENTER);
    miniText1.setText(R.string.zh);
    rowUnder.addView(miniText1);

    TextView miniText2 = new TextView(this);
    miniText2.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
    ));
    miniText2.setTextColor(Color.BLACK);
    miniText2.setGravity(Gravity.CENTER);
    miniText2.setText(R.string.u);
    rowUnder.addView(miniText2);
    tableLayout.addView(rowUnder);

    LinearLayout textRow2 = new LinearLayout(this);
    textRow2.setOrientation(LinearLayout.HORIZONTAL);
    textRow2.setPadding(50, 0, 50, 0);
    TextView gram = new TextView(this);
    gram.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
    ));
    gram.setTextSize(18);
    gram.setTypeface(Typeface.create("@font/inter", Typeface.ITALIC));
    gram.setTextColor(Color.BLACK);
    gram.setGravity(Gravity.CENTER);
    gram.setText(String.valueOf(note.getGram()));
    textRow2.addView(gram);

    TextView kilo = new TextView(this);
    kilo.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
    ));
    kilo.setTextSize(18);
    kilo.setTypeface(Typeface.create("@font/inter", Typeface.ITALIC));
    kilo.setTextColor(Color.BLACK);
    kilo.setGravity(Gravity.CENTER);
    kilo.setText(String.valueOf(Math.round(note.getKilo() * note.getGram() / 100 * 100.0) / 100.0));
    textRow2.addView(kilo);
    tableLayout.addView(textRow2);

    LinearLayout rowUnder2 = new LinearLayout(this);
    rowUnder2.setOrientation(LinearLayout.HORIZONTAL);
    rowUnder2.setPadding(50, 0, 50, 0);

    TextView miniG = new TextView(this);
    miniG.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
    ));
    miniG.setTextColor(Color.BLACK);
    miniG.setGravity(Gravity.CENTER);
    miniG.setText(R.string.zh);
    miniG.setText(R.string.g);
    rowUnder2.addView(miniG);

    TextView miniKkal = new TextView(this);
    miniKkal.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1
    ));
    miniKkal.setTextColor(Color.BLACK);
    miniKkal.setGravity(Gravity.CENTER);
    miniKkal.setText(R.string.zh);
    miniKkal.setText(R.string.kkal);
    rowUnder2.addView(miniKkal);
    tableLayout.addView(rowUnder2);
    linearLayout.addView(tableLayout);
    makeButtonFood(note, linearLayout);
    foodLayout.addView(linearLayout);
    }

    private void makeButtonFood(Note note, LinearLayout linearLayout){
        LinearLayout tableLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 15, 10, 15);
        tableLayout.setLayoutParams(params);
        tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        tableLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout tableRow = new LinearLayout(this);
        ImageButton imageButton = new ImageButton(this);
        imageButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        imageButton.setPadding(17, 17, 17, 17);
        imageButton.setImageResource(R.mipmap.edit);
        imageButton.setTag(note.getId());
        imageButton.setOnClickListener(this::goToEditNotes);
        tableRow.addView(imageButton);
        tableLayout.addView(tableRow);

        LinearLayout tableRow2 = new LinearLayout(this);
        ImageButton imageButton2 = new ImageButton(this);
        imageButton2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        imageButton2.setPadding(17, 17, 17, 17);
        imageButton2.setTag(note.getId());
        imageButton2.setImageResource(R.mipmap.delete);
        imageButton2.setOnClickListener(this::deleteNote);
        tableRow2.addView(imageButton2);
        tableLayout.addView(tableRow2);
        linearLayout.addView(tableLayout);
    }

    public Map<String, List<Note>> getDataGroupedByDateAndSortedById() {
        RealmResults<Note> results = realm.where(Note.class)
                .findAll();
        if (!results.isEmpty()) {
            results = results.sort("id", Sort.ASCENDING);
            results = results.sort("createdDate", Sort.ASCENDING);
            List<Note> notes = realm.copyFromRealm(results);
            Map<String, List<Note>> groupedData = new HashMap<>();
            for (Note note : notes) {
                String date = note.getCreatedDate();
                if (!groupedData.containsKey(date)) {
                    groupedData.put(date, new ArrayList<>());
                }
                Objects.requireNonNull(groupedData.get(date)).add(note);
            }
            return groupedData;
        }
        return null;
    }

    private void makeSummary(List<Note> notes){
        double totalProtein = Math.round(notes.stream()
                .mapToDouble(note -> note.getProtein() * note.getGram() / 100).sum() * 100.0) / 100.0;
        double totalFat = Math.round(notes.stream()
                .mapToDouble(note -> note.getFat() * note.getGram() / 100).sum() * 100.0) / 100.0;
        double totalCarbon = Math.round(notes.stream()
                .mapToDouble(note -> note.getCarbon() * note.getGram() / 100).sum() * 100.0) / 100.0;
        double totalKilo =  Math.round(notes.stream()
                .mapToDouble(note -> note.getKilo() * note.getGram() / 100).sum() * 100.0) / 100.0;

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 20, 20, 50);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundResource(R.drawable.stroke_corners);

        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params1.setMargins(30, 20, 20, 20);
        textView.setLayoutParams(params1);
        textView.setText(R.string.result);
        textView.setTextSize(25);
        textView.setTypeface(Typeface.create("@font/inter", Typeface.BOLD_ITALIC));
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(textView);

        LinearLayout tableLayout = new LinearLayout(this);
        tableLayout.setLayoutParams(new LinearLayout.LayoutParams (
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        tableLayout.setOrientation(LinearLayout.VERTICAL);
        tableLayout.setPadding(15, 15, 15, 15);

        LinearLayout textRow = new LinearLayout(this);
        textRow.setOrientation(LinearLayout.HORIZONTAL);
        TextView textProtein = new TextView(this);
        textProtein.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        textProtein.setText(String.valueOf(totalProtein));
        textProtein.setTextSize(18);
        textProtein.setTypeface(Typeface.create("@font/inter", Typeface.ITALIC));
        textProtein.setTextColor(Color.BLACK);
        textProtein.setGravity(Gravity.CENTER);
        textRow.addView(textProtein);

        TextView textFat = new TextView(this);
        textFat.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        textFat.setTextSize(18);
        textFat.setTextColor(Color.BLACK);
        textFat.setTypeface(Typeface.create("@font/inter", Typeface.ITALIC));
        textFat.setGravity(Gravity.CENTER);
        textFat.setText(String.valueOf(totalFat));
        textRow.addView(textFat);

        TextView textCarbon = new TextView(this);
        textCarbon.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        textCarbon.setTextSize(18);
        textCarbon.setTypeface(Typeface.create("@font/inter", Typeface.ITALIC));
        textCarbon.setTextColor(Color.BLACK);
        textCarbon.setGravity(Gravity.CENTER);
        textCarbon.setText(String.valueOf(totalCarbon));
        textRow.addView(textCarbon);

        TextView textKilo = new TextView(this);
        textKilo.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        textKilo.setTextSize(18);
        textKilo.setTypeface(Typeface.create("@font/inter", Typeface.ITALIC));
        textKilo.setTextColor(Color.BLACK);
        textKilo.setGravity(Gravity.CENTER);
        textKilo.setText(String.valueOf(totalKilo));
        textRow.addView(textKilo);
        tableLayout.addView(textRow);

        LinearLayout rowUnder = new LinearLayout(this);
        rowUnder.setOrientation(LinearLayout.HORIZONTAL);
        TextView miniTextB = new TextView(this);
        miniTextB.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        miniTextB.setText(R.string.b);
        miniTextB.setTextColor(Color.BLACK);
        miniTextB.setGravity(Gravity.CENTER);
        rowUnder.addView(miniTextB);
        TextView miniTextZh = new TextView(this);
        miniTextZh.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        miniTextZh.setTextColor(Color.BLACK);
        miniTextZh.setGravity(Gravity.CENTER);
        miniTextZh.setText(R.string.zh);
        rowUnder.addView(miniTextZh);

        TextView miniTextU = new TextView(this);
        miniTextU.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        miniTextU.setTextColor(Color.BLACK);
        miniTextU.setGravity(Gravity.CENTER);
        miniTextU.setText(R.string.u);
        rowUnder.addView(miniTextU);

        TextView miniTextKkal = new TextView(this);
        miniTextKkal.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        miniTextKkal.setTextColor(Color.BLACK);
        miniTextKkal.setGravity(Gravity.CENTER);
        miniTextKkal.setText(R.string.kkal);
        rowUnder.addView(miniTextKkal);

        tableLayout.addView(rowUnder);
        linearLayout.addView(tableLayout);
        foodLayout.addView(linearLayout);
    }

    public void printAllChildren(View view) {
        if (view instanceof ViewGroup) {
            Log.d("View", view.toString());
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                printAllChildren(child);
            }
        } else {
            Log.d("View", view.toString());
        }
    }
}
