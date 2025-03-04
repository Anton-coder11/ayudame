package org.example;


import java.util.HashMap;

public class getUserNames {
    static HashMap<String, String> movieTitles = new HashMap<>();
    public static void main(String[] args) {
        addToMyMap("1", "The Shawshank Redemption");
        addToMyMap("2", "The Godfather");
        addToMyMap("3", "The Dark Knight");





        //  Db.save("Hello1");
        System.out.println(usersDb.load());
    }

    public static void addToMyMap(String key,String value) {//
        movieTitles.put(key, value);
    }
    public static void removeFromMyMap(String key) {
        movieTitles.remove(key);
    }
    public static String getFromMyMap(String key) {
        return movieTitles.get(key);
    }

    public static String getInfoForValue(String value) {
        String s = "1";
        String s1 = "2";
        if (s.equals(s1))
            System.out.println("s==s1");

        for (String key : movieTitles.keySet()) {
            if (movieTitles.get(key).equals(value)) {
                return "Ключ: " + key + ", Значення: " + value;
            }

        }
        return "Значення не знайдено";
    }
}
