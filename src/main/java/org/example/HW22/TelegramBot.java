package org.example.HW22;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.example.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {

    private final String BOT_USERNAME = "JavaHomeWorkBot";
    private final String BOT_TOKEN = "7863943436:AAHa7ysYG4dLBRBWjhsr0aMsf2Lg4M5T4-M";
    private final String  ADMIN_ID ="753444383";
    private  String CLIENT_ID = "";
    private String USERNAME;
    private final String FILE = "/Users/anton/Desktop/coding/HILEL/JAVA/Pupa/src/main/java/org/example/HW22/usersDataBase.json";
    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    public void saveUsers(){
            // Пример новых пользователей
            List<TelegramUser> newTUsers = new ArrayList<>();
            newTUsers.add(new TelegramUser(CLIENT_ID, USERNAME, isAdmin()));


            // Создаем объект Gson
            Gson gson = new Gson();

            try {
                // Открываем файл для записи
                FileWriter writer = new FileWriter(FILE);

                // Сериализуем список пользователей в JSON и записываем в файл
                gson.toJson(newTUsers, writer);

                // Закрываем файл
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }



    public void loadUser() {
        try {
            // Открываем JSON-файл
            FileReader reader = new FileReader("src/main/java/org/example/HW22/usersDataBase.json");

            // Создаем объект Gson
            Gson gson = new Gson();

            // Определяем тип данных
            Type listType = new TypeToken<List<TelegramUser>>(){}.getType();

            // Десериализация JSON в список объектов User
            List<TelegramUser> users = gson.fromJson(reader, listType);

            // Закрываем файл
            reader.close();

            // Перебираем пользователей и извлекаем интересующие данные
            for (TelegramUser tUser : users) {
                // Пример: выводим имена пользователей
                System.out.println(tUser.getId() + "   @" + tUser.getUsername());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public boolean isAdmin() {
        return CLIENT_ID.equals(ADMIN_ID);
    }
    @Override
    public void onUpdateReceived(Update update) {
        CLIENT_ID = update.getMessage().getChatId().toString();
        USERNAME = update.getMessage().getFrom().getUserName();
    saveUsers();
        // Check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            SendMessage response = new SendMessage();
            response.setChatId(String.valueOf(chatId));
            response.setText("You said: " + messageText);

            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            loadUser();
        }
    }

    public static void main(String[] args) {
        try {
            // Initialize Api Context
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot());

            System.out.println("Bot started successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
