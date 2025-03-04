package org.example;

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
    public static String MY_CHAT_ID = "753444383";
    public static String CLIENT_ID = "";
    String USERNAME_GLOBAL;


    private static final String USERS_FILE = "/Users/anton/Desktop/coding/HILEL/JAVA/Pupa/src/main/java/org/example/users.txt";
    private Set<String> allUsers;
    boolean isOwner = CLIENT_ID.equals(MY_CHAT_ID);
    public HelloWorldBot() {
        allUsers = loadUsers();
        onstart();

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
                        System.out.println("Loaded user: " + line.trim());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
    private void saveUsers(Set<String> users) {
        try (FileWriter writer = new FileWriter(USERS_FILE)) {
            for (String userId : users) {
                writer.write(userId +"\n");
            }
            System.out.println("Saved " + users.size() + " users to file");
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void onstart() {
        String startMessage = "Привет! \n" +
                "Список команд:\n" +
                "/send - отправить сообщение\n" +
                "/game - начать игру\n" +
                "/stop - остановить игру\n" +
                "/help - список команд";

            for (String userId : allUsers) {
                try {
                    sendMsg(userId, startMessage);
                    Thread.sleep(35); // Avoid Telegram rate limits
                } catch (Exception e) {
                    System.out.println("Failed to send startup message to: " + userId);
                }
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
                System.out.println("New user added: " + CLIENT_ID + " (" + USERNAME_GLOBAL + ")");
            }
            System.out.println(MY_CHAT_ID + " Клиентик -  "+CLIENT_ID + " " +  username);
            if(!CLIENT_ID.equals(MY_CHAT_ID)) {

                sendMsg(MY_CHAT_ID, " Клиентик -  '"+CLIENT_ID+ "'"+ "'" +username +"' отослал вам сообщение - '"+messageText+"'");

            }

            // Check for "Пока" first
            if (messageText.equalsIgnoreCase("пока") || messageText.equalsIgnoreCase("пока!")) {
                sendMsg(CLIENT_ID, "Прощай");
            }
            else if (messageText.equalsIgnoreCase("привет") || messageText.equalsIgnoreCase("привет!")) {
                sendMsg(CLIENT_ID, "Привет!");
            }
            help(CLIENT_ID,messageText);
            sendPrivateMsg(CLIENT_ID, messageText);
            game(messageText);

        }
    }


    private int tries = 3;

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
String lastWordLetter;
    String firstRespLetter;
    String speak;
    private boolean isGameActive = false;
    String word= "Воробей";
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
                sendMsg(CLIENT_ID, "Осталось попыок: " + tries);
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
    String  id;
    String message;
    boolean isSendable = false;
public void sendPrivateMsg(String chatId,String messageText ) {

    if (CLIENT_ID.equals(MY_CHAT_ID) && messageText.equals("/send") || isSendable ) {

        String[] parts = messageText.split("/", 2);// limit=2 to split only on first "/"
        isSendable = true;

        if (parts.length == 2) {  // Check if message contains "/"
            id = parts[0].trim();  // Get ID (before /)
            message = parts[1].trim();  // Get message (after /)

            if (messageText.contains("/")) {
                if (!messageText.equals("/send")) {

                    isSendable = false;
                sendMsg(id, message);
                sendMsg(MY_CHAT_ID, "Сообщение отправлено!");
                }
                else {
                    sendMsg(chatId, "Кому хотите написать сооббщение? (Введите ID / сообщение)");
                }

            }
        }
    }
    else if(!CLIENT_ID.equals(MY_CHAT_ID)&&messageText.equals("/send")) {
        sendMsg(MY_CHAT_ID, "Пользователь - " + USERNAME_GLOBAL +" попытался использовать заблокировануюю команду");
        sendMsg(chatId, " у вас недостаточно доступа для использования этой команды");
    }
}

public void help(String chatId,String messageText){
    String [] users = usersDb.load().split("\n");
    if(messageText.equals("/help")){
        sendMsg(chatId, "Список команд: \n /send - отправить сообщение \n /game - начать игру \n /stop - остановить игру \n /help - список команд");
        if (CLIENT_ID.equals(MY_CHAT_ID)){
            sendMsg(chatId, "Пользователи - \n" + Arrays.toString(users));
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

public static void main(String[] args) {
    try {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new HelloWorldBot());
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }


}
}
class Timer extends Thread{
    private final HelloWorldBot helloWorldBot;
    public Timer(HelloWorldBot helloWorldBot){
        this.helloWorldBot = helloWorldBot;
        start();
    }

    @Override
    public void run() {
        int i = 0;
        while (true){
            try {
                if (i==5){
                    helloWorldBot.sendMsg(HelloWorldBot.MY_CHAT_ID, "привет 5");
                }
                if (i==15){
                    helloWorldBot.sendMsg(HelloWorldBot.MY_CHAT_ID, "привет 15");
                }
                if (i==25){
                    helloWorldBot.sendMsg(HelloWorldBot.MY_CHAT_ID, "привет 25");
                }
                i++;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}


