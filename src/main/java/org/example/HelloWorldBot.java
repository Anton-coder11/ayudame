package org.example;

import org.example.TELEGRAM.jokes.apiRequest;
import org.example.TELEGRAM.translator.Translator;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

public class HelloWorldBot extends TelegramLongPollingBot {
    private final String MY_CHAT_ID = "753444383";
    private final String cooAdmin = "1007868278";
    public static String CLIENT_ID = "";
    String USERNAME_GLOBAL;
    String lastWordLetter;
    String firstRespLetter;
    String speak;
    private boolean isGameActive = false;
    String word= "Воробей";
    private int tries = 3;
    String  id;
    String message;
    boolean isSendable = false;
    String weatherRequest;
    boolean joking =true;
    private String announcementState = "none";  // Use your existing variable if you have one
     String [] users = usersDb.load().split("\n");
    boolean weatherIsSendable = false;

    private static final String USERS_FILE = "src/main/java/org/example/users.txt";
    private final Set<String> allUsers;

    public HelloWorldBot() {
        allUsers = loadUsers();
        onstart();
        sendMsg(MY_CHAT_ID,"Привет, босс!");

    }
    private Set<String> loadUsers() {
        Set<String> users = new HashSet<>();
        File file = new File(USERS_FILE);

        // Create directory if it doesn't exist
        file.getParentFile().mkdirs();

        try {
            if (!file.exists()) {
                file.createNewFile();
                // Add admin as first user
                users.add(MY_CHAT_ID);
                saveUsers(users);
                System.out.println("Created new users file with admin ID");
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        users.add(line.trim());
                        System.out.println("Пользователь загружен:" + line.trim());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке пользователей:" + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
    private void saveUsers(Set<String> users) {
        try (FileWriter writer = new FileWriter(USERS_FILE)) {
            for (String userId : users) {
                writer.write(userId +"\n");
            }
            System.out.println("Сохранено " + users.size() + " пользователей в файл.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении пользователей:" + e.getMessage());
            e.printStackTrace();
        }
    }
    public void onstart() {
        if (CLIENT_ID.equals(MY_CHAT_ID)){
            sendMsg(CLIENT_ID, "Админ онлайн");
        }
     }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            CLIENT_ID = update.getMessage().getChatId().toString();
            // Add new user if not already in the set


            String messageText = update.getMessage().getText();
            String username = update.getMessage().getFrom().getUserName();

            USERNAME_GLOBAL="@"+username;
            if (!allUsers.contains(CLIENT_ID)) {
                allUsers.add(CLIENT_ID);
                saveUsers(allUsers);  // Save updated user list
                System.out.println("Новый пользователь: " + CLIENT_ID + " (" + USERNAME_GLOBAL + ")");
            }
            System.out.println(MY_CHAT_ID + " Клиентик -  "+CLIENT_ID + " " +  USERNAME_GLOBAL);

            if(!CLIENT_ID.equals(MY_CHAT_ID)) {

                sendMsg(MY_CHAT_ID, " Клиентик -  '"+CLIENT_ID+ "'"+ "'" +USERNAME_GLOBAL +"' отослал вам сообщение - '"+messageText+"'");

            }

            speaking(messageText);
            getWeather(messageText);
            help(CLIENT_ID,messageText);
            sendPrivateMsg(CLIENT_ID, messageText);
            game(messageText);
//            announce(CLIENT_ID,messageText);
            tellJoke(messageText);

        }
    }

    public void speaking(String messageText){
        // Check for "Пока" first
        if (messageText.equalsIgnoreCase("пока") || messageText.equalsIgnoreCase("пока!")) {
            sendMsg(CLIENT_ID, "Прощай");
        }
        else if (messageText.equalsIgnoreCase("привет") || messageText.equalsIgnoreCase("привет!")) {
            sendMsg(CLIENT_ID, "Привет!");
        }
        else if (messageText.equalsIgnoreCase("как дела") || messageText.equalsIgnoreCase("как дела?")) {
            sendMsg(CLIENT_ID, "Нормально");
        }

    }
    public  void sendMsg(String chatId, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);

        try {
            execute(message);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
}
    private void game(String messageText) {

        if (messageText.equals("/game")) {
            isGameActive = true;
            sendMsg(CLIENT_ID, "Привет!");
            sendMsg(CLIENT_ID, "Слово: Воробей");
            speak = "";
            lastWordLetter = String.valueOf(word.charAt(word.length()-1));
            return;
        }
        else if (messageText.equals("/stop")) {
            isGameActive = false;
            sendMsg(CLIENT_ID, "Игра окончена! Спасибо за участие!");
            lastWordLetter = null;
            firstRespLetter = null;
            speak = null;
            return;
        }

        if (isGameActive) {

            if (String.valueOf(messageText.charAt(0)).equals(lastWordLetter)) {
                speak = "Nice";
                word =messageText ;
                lastWordLetter = String.valueOf(messageText.charAt(messageText.length() - 1));
                sendMsg(CLIENT_ID, speak);
                firstRespLetter = String.valueOf(messageText.charAt(0));
                sendMsg(CLIENT_ID, "Следующее слово: " + messageText);

            } else {
                tries--;
                sendMsg(CLIENT_ID, "bad");
                sendMsg(CLIENT_ID, "Осталось попыток: " + tries);
                if(tries <= 0){
                    sendMsg(CLIENT_ID, "Игра окончена! Спасибо за участие! Напишите /game если хотите сыграть еще раз.") ;
                    isGameActive = false;
                    lastWordLetter = null;
                    firstRespLetter = null;
                    speak = null;
                    tries = 3;
                }

            }
        }
    }
    public void tellJoke(String messageText) {
         if(messageText.equals("/joke")){
            try {
                joking = true;
                sendMsg(CLIENT_ID, apiRequest.getJoke());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

         }
         else if (messageText.equalsIgnoreCase("еще")&& joking==true){
            try {
                sendMsg(CLIENT_ID, apiRequest.getJoke());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
         else if (messageText.equalsIgnoreCase("хватит")){
            sendMsg(CLIENT_ID, "Хорошо , больше не шутим");
            joking = false;
        }

    }
    public void announce(String chatId, String messageText) {
        if (CLIENT_ID.equals(MY_CHAT_ID)) {
            if (messageText.equals("/announce")) {
                announcementState = "waiting";
                sendMsg(chatId, "Пожалуйста введите сообщение которое хотите отослать всем");
            }
            else if (announcementState.equals("waiting")) {
                announcementState = "none";  // Reset immediately
                String[] users = usersDb.load().split("\n");
                for (String userId : users) {
                    if (userId != null && !userId.trim().isEmpty()) {
                        sendMsg(userId.trim(), messageText);
                    }
                }
                sendMsg(MY_CHAT_ID, "Сообщение отправлено всем!");
            }
        } else {
            sendMsg(chatId, "У вас нету прав на использование этой команды!");
        }
    }

    public void sendPrivateMsg(String chatId,String messageText ) {

    if (CLIENT_ID.equals(MY_CHAT_ID) && messageText.equals("/send") ||CLIENT_ID.equals(cooAdmin)&& messageText.equals("/send") ||isSendable ) {

        String[] parts = messageText.split("/", 2);// limit=2 to split only on first "/"
        isSendable = true;

        if (parts.length == 2) {  // Check if message contains "/"
            id = parts[0].trim();  // Get ID (before /)
            message = parts[1].trim();  // Get message (after /)

            if (messageText.contains("/")) {
                if (!messageText.equals("/send")) {
                    isSendable = false;
                    sendMsg(id, "Администатор отправил вам сообщение: \n" +message);
                    sendMsg(MY_CHAT_ID, "Сообщение отправлено!");
                }
                else {
                    sendMsg(chatId, "Кому хотите написать сооббщение? (Введите ID / сообщение)");
                    sendMsg(chatId, "Пользователи - \n" + Arrays.toString(users));

                }

            }
        }
    }
    else if(!CLIENT_ID.equals(MY_CHAT_ID)&&messageText.equals("/send")) {
        sendMsg(MY_CHAT_ID, "Пользователь - " + USERNAME_GLOBAL +" попытался использовать заблокировануюю команду");
        sendMsg(chatId, " У вас недостаточно доступа для использования этой команды");
    }
}

    public void help(String chatId,String messageText){
    if(messageText.equals("/help")){
        sendMsg(chatId, "Список команд: \n /send - Отправить сообщение \n /game - Начать игру \n /stop - Остановить игру \n /help - Список команд \n /announce - Оповещение пользователей \n /weather - Показывает погоду в выбраном городе");
        if (CLIENT_ID.equals(MY_CHAT_ID) || CLIENT_ID.equals(cooAdmin)){
            sendMsg(chatId, "Пользователи - \n" + Arrays.toString(users));
        }


    }
}
    public void getWeather(String messageText){
    if((messageText.equals("/weather") || weatherIsSendable)){
        if (!messageText.equals("/weather")) {
            weatherIsSendable=false;
            if (isEnglish(messageText)){
                try {
                    weatherRequest = Translator.translate(messageText);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if (isRussian(messageText)){
                weatherRequest = messageText;
            }


            try {
                sendMsg(CLIENT_ID, WeatherMain.getWeather(weatherRequest));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else {
            sendMsg(CLIENT_ID, "Введите город");
        }
        if(messageText.equals("/weather")){
            weatherIsSendable=true;
        }
    }
}
    @Override
    public String getBotUsername() {
    return "Pupaiaiaiaiiaaaiibot";
}

    @Override
    public String getBotToken() {
    return "7822239652:AAGA_JKpYhLvMBeg_M8ZnPwyoMDRKln1_f4";
}
    public boolean isEnglish(String text) {
        return text.matches("[a-zA-Z\\s]+");
    }

    public boolean isRussian(String text) {
        return text.matches("[а-яА-ЯёЁ\\s]+");
    }



public static void main(String[] args) {
    try {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new HelloWorldBot());
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }


}
}


