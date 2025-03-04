package org.example;

import java.io.*;

public class usersDb {
    private static final String FIFE_NAME = "/Users/anton/Desktop/coding/HILEL/JAVA/Pupa/src/main/java/org/example/users.txt";

    public  static void save(String data){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FIFE_NAME))) {
            writer.write(data);
            System.out.println("Файл успішно збережено.");
        } catch (IOException e) {
            System.err.println("Помилка збереження файлу: " + e.getMessage());
        }
    }
    public static String load(){
        String content = "";//hello+"\n"123"\n"
        try (BufferedReader reader = new BufferedReader(new FileReader(FIFE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content+=line+"\n";
            }
        } catch (IOException e) {
            System.err.println("Помилка завантаження файлу: " + e.getMessage());
        }
        return content;
    }
}


