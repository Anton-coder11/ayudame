package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class HelloWorldBot extends TelegramLongPollingBot {
    public static String MY_CHAT_ID = "753444383";
    public static String CLIENT_ID = "";
     public HelloWorldBot(){
         onstart();

     }
    public void onstart() {
        sendMsg(MY_CHAT_ID, "Привет!");

    }
@Override
public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
        CLIENT_ID = update.getMessage().getChatId().toString();

        String messageText = update.getMessage().getText();
        String username = update.getMessage().getFrom().getUserName();
        System.out.println(MY_CHAT_ID + "Клиентик -  "+CLIENT_ID + " " +  username);
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
        sendPrivateMsg(CLIENT_ID, messageText, messageText);
        game(messageText);
    }
}


    public void sendMsg(String chatId, String msg) {
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
                sendMsg(CLIENT_ID, "bad");
            }
        }
    }
    String  id;
    String message;
//public void sendPrivateMsg(String chatId, String msg,String messageText) {
//
//    if (CLIENT_ID.equals(MY_CHAT_ID)&&messageText.equals("/send")) {
//        sendMsg(chatId, "Кому хотите написать сооббщение? (Введите ID / сообщение)");
//        String[] parts = messageText.split("/", 2);  // limit=2 to split only on first "/"
//        if (parts.length == 2) {  // Check if message contains "/"
//            id = parts[0].trim();  // Get ID (before /)
//            message = parts[1].trim();  // Get message (after /)
//            if (messageText.contains("/")) {
//                sendMsg(id, message);
//            }
//        }
//
//    }
//    else {
//        sendMsg(chatId,"Вы не имеете привелегий такого масштаба");
//    }



//    String message;
//    if (CLIENT_ID.equals(MY_CHAT_ID)&&messageText.equals("/send")) {
//        sendMsg(MY_CHAT_ID,"Кому хотите написать сооьбщение (Введите ID)");
//        return;
//    }
//    if (Integer.parseInt(msg) == 1007868278) {
//        id = Integer.parseInt(msg);
//        sendMsg(MY_CHAT_ID, "Введите сообщение");
//        sendMsg(MY_CHAT_ID,msg);
//
//        sendMsg(String.valueOf(id), msg);
//        sendMsg(MY_CHAT_ID, "Сообщение отправлено!");
//    }
//    else {
//        sendMsg(MY_CHAT_ID, "Неверный формат ID. Попробуйте снова /send");
//
//    }
//    else {
//        sendMsg(CLIENT_ID, "Команда не доступна");
//        return;
//    }
public void sendPrivateMsg(String chatId, String msg, String messageText) {
    // First check if user has permission
    if (!CLIENT_ID.equals(MY_CHAT_ID)) {
        sendMsg(chatId, "Вы не имеете привилегий такого масштаба");
        return;
    }

    // Handle /send command
    if (messageText.equals("/send")) {
        sendMsg(chatId, "Введите сообщение в формате: ID/сообщение");
        return;
    }

    // Handle /stop command
    if (messageText.equals("/stop")) {
        // Reset any state variables you might have
        id = null;
        message = null;
        sendMsg(chatId, "Отправка сообщений остановлена");
        return;
    }

    // Handle messages with ID/message format
    if (messageText.contains("/")) {
        String[] parts = messageText.split("/", 2);  // limit=2 to split only on first "/"
        if (parts.length == 2) {
            String idPart = parts[0].trim();
            String messagePart = parts[1].trim();

            if (idPart.matches("\\d+")) {
                sendMsg(idPart, messagePart);
                sendMsg(chatId, "Сообщение отправлено!");
            } else {
                sendMsg(chatId, "Неверный формат ID. Используйте: ID/сообщение");
            }
        }
    } else {
        // Handle normal messages here
        // You can either ignore them or send a help message
        sendMsg(chatId, "Для отправки личного сообщения используйте формат: ID/сообщение или команду /send");
    }
}



}

//    private String waitingForId = null;  // To store the ID we're waiting for
//    private boolean waitingForMessage = false;  // To track if we're waiting for the message
//    private String targetId = null;  // To store the target ID
//
//    public void sendPrivateMsg(String chatId, String msg, String messageText) {
//        if (messageText.equals("/sendMSG")) {
//            waitingForId = chatId;  // Start waiting for ID
//            sendMsg(chatId, "Кому хотите написать сообщение? (Введите ID)");
//            return;
//        }
//
//        // If we're waiting for ID and got a response
//        if (waitingForId != null && waitingForId.equals(chatId)) {
//            try {
//                // Try to parse the ID to make sure it's valid
//                Long.parseLong(messageText);
//                targetId = messageText;
//                waitingForId = null;  // Stop waiting for ID
//                waitingForMessage = true;  // Start waiting for message
//                sendMsg(chatId, "Введите сообщение:");
//                return;
//            } catch (NumberFormatException e) {
//                sendMsg(chatId, "Неверный формат ID. Попробуйте снова /sendMSG");
//                waitingForId = null;
//                return;
//            }
//        }
//
//        // If we're waiting for the message and got a response
//        if (waitingForMessage && chatId.equals(chatId)) {
//            sendMsg(targetId, msg);  // Send message to target ID
//            sendMsg(chatId, "Сообщение отправлено!");
//            // Reset all flags
//            waitingForMessage = false;
//            targetId = null;
//            return;
//        }
//    }


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


