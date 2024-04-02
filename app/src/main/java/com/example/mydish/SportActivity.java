package com.example.mydish;

import static com.example.mydish.NewsApiClient.convertToDayMonthYear;
import static com.example.mydish.NewsApiClient.jsonArrayToList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class SportActivity extends LowerPanel {
    private static final String URL_SPORT = "https://newsdata.io/api/1/news?apikey=pub_411732c5bedfb0a4a3a16dba91479be4f0926&country=ru&language=ru&category=sports";
    LinearLayout mainNewsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes);
        ImageButton favoriteButton = findViewById(R.id.recipes_button);
        favoriteButton.setImageResource(R.mipmap.chosen_recipes);
        mainNewsLayout = findViewById(R.id.newsLayout);
        mainNewsLayout.removeAllViews();
        try {
            final String result = new NewsApiClient().getNews(URL_SPORT);
            if (result != null && !result.isEmpty()) {
                Log.d("Sport", result);
                try {
                        Log.d("Try", "Пытаюсь");
                        addNews(new JSONObject(result));
                        Log.d("Good", "Нормально");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
            } else {
                throw new RuntimeException();
            }
        } catch (IOException | InterruptedException | RuntimeException e) {
            Log.e("ErrorExit", e.toString());
            Toast.makeText(getApplicationContext(), "Ошибка запроса новостей спорта", Toast.LENGTH_SHORT).show();
        }
    }

    public void addNews(JSONObject result) throws JSONException {
        List<JSONObject> articles = jsonArrayToList((JSONArray) result.get("results"));
        for (JSONObject news : articles){
            LinearLayout newsLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(20, 20, 20, 20);
            newsLayout.setLayoutParams(layoutParams);
            newsLayout.setOrientation(LinearLayout.VERTICAL);
            newsLayout.setBackgroundResource(R.drawable.stroke_corners);

            TextView title = new TextView(this);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textParams.setMargins(20, 0, 20, 0);
            title.setLayoutParams(textParams);
            if (news.has("title")) {
                title.setText(news.getString("title"));
            }
            title.setTextSize(20);
            title.setTypeface(Typeface.create("@font/inter_semibold", Typeface.BOLD));
            title.setTextColor(Color.BLACK);
            newsLayout.addView(title);
            TextView description = new TextView(this);
            LinearLayout.LayoutParams descriptionParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            descriptionParams.setMargins(20, 0, 20, 0);
            description.setLayoutParams(descriptionParams);
            if (news.has("description")) {
                if (!news.getString("description").equals("null")) {
                    description.setText(news.getString("description"));
                }
            }
            description.setTextSize(17);
            newsLayout.addView(description);

            LinearLayout authorDateLayout = new LinearLayout(this);
            authorDateLayout.setLayoutParams(layoutParams);
            authorDateLayout.setOrientation(LinearLayout.HORIZONTAL);

            CardView cardView = new CardView(this);

            LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(20, 20, 20, 20);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            cardView.setLayoutParams(cardViewParams);
            cardView.setRadius(50);
            cardView.setCardElevation(1);
            ImageView imageView = new ImageView(this);
            if (news.has("source_icon")) {
                Picasso.get().load(news.getString("source_icon")).into(imageView);
            }
            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                    110,
                    110
            );
            imageViewParams.gravity = Gravity.TOP;
            imageView.setLayoutParams(imageViewParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            cardView.addView(imageView);
            authorDateLayout.addView(cardView);
            LinearLayout.LayoutParams miniTextParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            );
            miniTextParams.gravity = Gravity.CENTER_VERTICAL;
            TextView date = new TextView(this);
            date.setLayoutParams(miniTextParams);
            if (news.has("pubDate")) {
                date.setText(convertToDayMonthYear(news.getString("pubDate"), "yyyy-MM-dd HH:mm:ss"));
            }
            Log.d("DateText", date.getText().toString());
            date.setTextSize(17);
            date.setGravity(Gravity.END);
            authorDateLayout.addView(date);
            newsLayout.addView(authorDateLayout);
            if (news.has("link")) {
                newsLayout.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getString("link")));
                        startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            mainNewsLayout.addView(newsLayout);
        }

    }




    //private boolean isFavorite = false;
    //public void makeUnmakeFavorite(View view) {
    //    ImageButton favoriteButton = (ImageButton) view;
    //    if (isFavorite) {
    //        favoriteButton.setImageResource(R.mipmap.favorites_mini);
    //    } else {
    //        favoriteButton.setImageResource(R.mipmap.made_favorite);
    //    }
    //    isFavorite = !isFavorite;
    //}



    public void goToRecipe(View view) {
        Intent intent = new Intent(this, ChosenRecipeActivity.class);
        startActivity(intent);
    }
}
