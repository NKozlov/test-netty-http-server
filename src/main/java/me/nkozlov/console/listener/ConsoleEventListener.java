/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console.listener;

import me.nkozlov.console.ConsoleEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Листенер консоли. "Слушает" клавиатуру пользователя и, после нажатия Enter, считывает консоль и отдает обработчику {@link ConsoleEventHandler}. <br/>
 * <p/>
 * Singleton. Инициализируется в IoC-контейнере. Поток создается и запускается из {@link me.nkozlov.Bootstrap}
 *
 * @author Kozlov Nikita
 * @see ConsoleEventHandler
 */
public class ConsoleEventListener implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleEventListener.class);

    @Autowired
    ConsoleEventHandler consoleEventHandler;

    @Override
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        boolean exitFlag = false;

        // слушаем, пока exitFlag = false.
        while (!exitFlag) {
            try {
                System.out.print(">");
                line = in.readLine();
            } catch (IOException e) {
                logger.error("[{}]: {}", Thread.currentThread().getName(), e.getMessage());
                throw new RuntimeException(e);
            }
            exitFlag = consoleEventHandler.processEvent(line);
        }

        try {
            in.close();
        } catch (IOException e) {
            logger.error("[{}]: {}", Thread.currentThread().getName(), e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
