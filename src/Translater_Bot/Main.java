package Translater_Bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final ThreadLocal<Translater> translater = ThreadLocal.withInitial(Translater::new);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {
        TelegramBot telegramBot = new TelegramBot("7522245044:AAFLZHp5VfYUt_YjLO50gTQQN16ssnbnNpU");
        telegramBot.setUpdatesListener((updates) -> {
            for (Update update : updates) {
                CompletableFuture.runAsync(() -> {
                    try {
                        translater.get().translate(update);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },executorService);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, Throwable::printStackTrace);
    }
}
