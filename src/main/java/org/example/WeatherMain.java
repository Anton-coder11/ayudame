package org.example;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherMain
{

    private static final String API_KEY = "bfebbff956c2f268f300d3bed559e225"; // Замініть на свій ключ API
    private static final String CITY_NAME = " киев"; // Замініть на назву міста

    public static void main(String[] args) {
        try {
            String weatherData = getWeatherData(CITY_NAME);
            JSONObject json = new JSONObject(weatherData);

            // Отримання необхідних даних з JSON
            JSONObject main = json.getJSONObject("main");

            double temperature = main.getDouble("temp");
            double humidity = main.getDouble("humidity");

            JSONObject wind = json.getJSONObject("wind");
            double windSpeed = wind.getDouble("speed");

            JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
            String description = weather.getString("description");

            // Виведення результатів
            System.out.println("Погода в " + CITY_NAME + ":");
            System.out.println("Температура: " + temperature + "°C");
            System.out.println("Вологість: " + humidity + "%");
            System.out.println("Швидкість вітру: " + windSpeed + " м/с");
            System.out.println("Опис: " + description);

        } catch (IOException e) {
            System.err.println("Помилка при отриманні даних: " + e.getMessage());
        }
    }

    public static String getWeatherData(String cityName) throws IOException {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + API_KEY + "&units=metric" + "&lang=ru";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        return response.toString();
    }
    public static String capitalizeEachWord(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return sentence;
        }

        String[] words = sentence.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    public static String getWeather(String city) throws IOException {
        String weatherData = getWeatherData(city);
        JSONObject json = new JSONObject(weatherData);
        JSONObject main = json.getJSONObject("main");
        double temperature = main.getDouble("temp");
        double humidity = main.getDouble("humidity");
        JSONObject wind = json.getJSONObject("wind");
        double windSpeed = wind.getDouble("speed");
        JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");
        return  ("Погода в городе - " + capitalizeEachWord(city) + "\n"+"Температура: " + temperature + "\n" +"Влажность: "+ humidity +"\n"+ "Скорость ветра: "+windSpeed+"\n"+"Погода: " +description);
    }
}