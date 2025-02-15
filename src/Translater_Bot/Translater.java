package Translater_Bot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import okhttp3.*;

import java.util.concurrent.ConcurrentHashMap;

public class Translater {
    private final TelegramBot telegramBot = new TelegramBot("7522245044:AAFLZHp5VfYUt_YjLO50gTQQN16ssnbnNpU");
    private static final ConcurrentHashMap<Long, Language_State> userState = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, String> chosenLanguage = new ConcurrentHashMap<>();

    private static final String API_URL = "https://translate.api.cloud.yandex.net/translate/v2/translate";
    private static final String OAUTH_TOKEN = "y3_Vdheub7w9bIut67GHeL345gfb5GAnd3dZnf08FRbvjeUFvetYiohGvc";
    private static final String FOLDER_ID = "b1g36c3rii6g8tedd7pr";

    private static final OkHttpClient client = new OkHttpClient();

    public void translate(Update update) throws Exception {
        Message message = update.message();
        CallbackQuery callbackQuery = update.callbackQuery();
        if (message != null) {
            String text = message.text();
            Chat chat = message.chat();
            Long chatID = message != null ? chat.id() : null;
            if (text.equals("/start")) {
                SendMessage sendMessage = new SendMessage(chatID, "Welcome to our @translaterchi_bot 😊\nTo begin bot send /translate");
                telegramBot.execute(sendMessage);
            } else if (text.equals("/translate")) {
                SendMessage sendMessage = new SendMessage(chatID, "Please, choose type of language 💁🏻:");

                languageButtons(sendMessage);

                telegramBot.execute(sendMessage);
            } else if (userState.getOrDefault(chatID, null) == Language_State.WAITING_LANGUAGE) {
                String userMessage = "";
                switch (text) {
                    case "\uD83C\uDDFA\uD83C\uDDFF" -> userMessage = "uz";
                    case "\uD83C\uDDF7\uD83C\uDDFA" -> userMessage = "ru";
                    case "\uD83C\uDDEC\uD83C\uDDE7" -> userMessage = "en";
                    case "\uD83C\uDDF0\uD83C\uDDF7" -> userMessage = "kr";
                    default -> {
                        AnswerCallbackQuery request = new AnswerCallbackQuery(callbackQuery.id());
                        request.showAlert(true);
                        request.text("No such type of language found :(\n        Please, try again.");
                        telegramBot.execute(request);
                        return;
                    }
                }
                chosenLanguage.put(chatID, userMessage);
                userState.put(chatID, Language_State.WAITING_TEXT);
                SendMessage sendMessage = new SendMessage(chatID, "Enter text you want to translate:");
                telegramBot.execute(sendMessage);
            } else if (userState.getOrDefault(chatID, null) == Language_State.WAITING_LANGUAGE) {
                String userLanguage = chosenLanguage.get(chatID);
                String translatedText = translateText(text, userLanguage);

                SendMessage sendMessage = new SendMessage(chatID, "Translation -> " + translatedText);
                telegramBot.execute(sendMessage);
            } else if (callbackQuery != null) {
                System.out.println(callbackQuery.data());
            }
        }
    }

    private String translateText(String text, String userLanguage) throws Exception {
        JsonObject requestBody = new JsonObject();
        requestBody.add("b1g36c3rii6g8tedd7pr", new JsonPrimitive(FOLDER_ID));
        requestBody.add("targetLanguageCode", new JsonPrimitive(userLanguage));

        JsonArray textArray = new JsonArray();
        textArray.add(text);
        requestBody.add("texts", textArray);

        RequestBody body = RequestBody.create(requestBody.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + OAUTH_TOKEN)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();  // API javobini olish
                JsonObject jsonRes = JsonParser.parseString(jsonResponse).getAsJsonObject();
                return jsonRes.getAsJsonArray("translations")
                        .get(0)
                        .getAsJsonObject()
                        .get("text")
                        .getAsString();
            }
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "!!!Error!!!";
    }

    private static void languageButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup buttons = new ReplyKeyboardMarkup(new KeyboardButton[][]{
                {new KeyboardButton("\uD83C\uDDFA\uD83C\uDDFF"), new KeyboardButton("\uD83C\uDDF7\uD83C\uDDFA")},
                {new KeyboardButton("\uD83C\uDDEC\uD83C\uDDE7"), new KeyboardButton("\uD83C\uDDF0\uD83C\uDDF7")}
        }).resizeKeyboard(true);
        sendMessage.replyMarkup(buttons);
    }
}

enum Language_State {
    WAITING_TEXT,
    WAITING_LANGUAGE;
}
