package org.example.TELEGRAM.jokes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.net.URL;

import org.example.TELEGRAM.translator.Translator;
import org.json.JSONObject;

public class apiRequest {
    public static String getJoke() throws Exception {
            String apiUrl = "https://api.chucknorris.io/jokes/random"; // Приклад API
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP Error: " + conn.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            JSONObject jsonResponse = new JSONObject(response.toString());
            String joke = jsonResponse.getString("value");


            conn.disconnect();
        return joke +"\n"+Translator.translate(joke);
    }
    public static String getOriginakJoke() throws Exception {
        String apiUrl = "https://api.chucknorris.io/jokes/random"; // Приклад API
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("HTTP Error: " + conn.getResponseCode());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        JSONObject jsonResponse = new JSONObject(response.toString());
        String joke = jsonResponse.getString("value");


        conn.disconnect();
        return joke;
    }

}



