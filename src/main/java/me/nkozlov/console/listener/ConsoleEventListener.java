/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console.listener;

import me.nkozlov.console.ConsoleEventHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Kozlov Nikita
 */
public class ConsoleEventListener implements Runnable {

    @Autowired
    ConsoleEventHandler consoleEventHandler;

    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = "";

        while (!line.equalsIgnoreCase("quit")) {
            try {
                System.out.print(">");
                line = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            consoleEventHandler.processEvent(line);
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
