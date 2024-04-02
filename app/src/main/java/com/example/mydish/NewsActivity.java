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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsActivity extends LowerPanel {
    private static final String URL_HEALTH = "https://newsapi.org/v2/top-headlines?country=ru&category=health&apiKey=2a14413d78a342ce86e702da234732f1";
    LinearLayout mainNewsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        ImageButton favoriteButton = findViewById(R.id.news_button);
        favoriteButton.setImageResource(R.mipmap.chosen_news);
        mainNewsLayout = findViewById(R.id.newsLayout);
        mainNewsLayout.removeAllViews();
        try {
            final String result = new NewsApiClient().getNews(URL_HEALTH);
            if (result != null && !result.isEmpty()) {
                Log.d("News", result);
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
        } catch (IOException | InterruptedException| RuntimeException e) {
            Log.e("ErrorExit", e.toString());
            Toast.makeText(getApplicationContext(), "Ошибка запроса новостей здоровья", Toast.LENGTH_SHORT).show();
        }
    }

    public void addNews(JSONObject result) throws JSONException {
        List<JSONObject> articles = jsonArrayToList((JSONArray) result.get("articles"));
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

            LinearLayout authorDateLayout = new LinearLayout(this);
            layoutParams.setMargins(20, 20, 20, 0);
            authorDateLayout.setLayoutParams(layoutParams);
            authorDateLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView author = new TextView(this);
            LinearLayout.LayoutParams miniTextParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            );
            author.setLayoutParams(miniTextParams);
            if (news.has("author")) {
                author.setText(news.getString("author"));
            }
            author.setTextSize(17);
            author.setGravity(Gravity.START);
            authorDateLayout.addView(author);
            TextView date = new TextView(this);
            date.setLayoutParams(miniTextParams);
            if (news.has("publishedAt")) {
                date.setText(convertToDayMonthYear(news.getString("publishedAt"), "yyyy-MM-dd'T'HH:mm:ss'Z'"));
            }
            date.setTextSize(17);
            date.setGravity(Gravity.END);
            authorDateLayout.addView(date);
            newsLayout.addView(authorDateLayout);
            if (news.has("url")) {
                newsLayout.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getString("url")));
                        Log.d("Url", Uri.parse(news.getString("url")).toString());
                        startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            mainNewsLayout.addView(newsLayout);
        }

    }



    public void goToNew(View view) {
        Intent intent = new Intent(this, ChosenNewActivity.class);
        startActivity(intent);
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
}
