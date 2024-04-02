package com.example.mydish;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsApiClient {
    private final OkHttpClient client = new OkHttpClient();
    public static List<JSONObject> jsonArrayToList(JSONArray jsonArray) {
        List<JSONObject> jsonObjectList = new ArrayList<>();

        if (jsonArray != null) {
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                try {
                    jsonObjectList.add(jsonArray.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonObjectList;
    }

    public static String convertToDayMonthYear(String dateString, String startFormat) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(startFormat);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");

            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNews(String url) throws IOException, InterruptedException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        String[] resultString = new String[1];
        Thread thread =  new Thread((Runnable) () -> {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    resultString[0] =  null;
                } else {
                    resultString[0] =  response.body() != null ? response.body().string() : null;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        thread.join();
        return resultString[0];
    }
}
