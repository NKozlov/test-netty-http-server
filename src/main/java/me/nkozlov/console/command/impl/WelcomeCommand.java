/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console.command.impl;

import me.nkozlov.console.command.AbstractCommand;
import me.nkozlov.console.command.RunCommand;

/**
 * Выводит приветствие в консоль с описанием информации о проекте.
 *
 * @author Kozlov Nikita
 * @see RunCommand
 * @see AbstractCommand
 */
public class WelcomeCommand extends AbstractCommand implements RunCommand {

    public WelcomeCommand() {
        this.setCommandName("welcome");
    }

    /**
     * Метод, который отвечает за запуск команды.
     */
    @Override
    public void apply() {
        StringBuilder welcomeMessage = new StringBuilder();
        welcomeMessage
                .append("######################################################################################################################\n")
                .append("###### == Welcome to the test http server, written Kozlova Nikita == ######\n")
                .append("## ~~~\n")
                .append("## = Home page for this project here: https://github.com/NKozlov/test-netty-http-server\n")
                .append("## = User manual here: https://github.com/NKozlov/test-netty-http-server/wiki/User-Manual\n")
                .append("## = Requirements for the development here: https://github.com/NKozlov/test-netty-http-server/wiki/Requirements\n")
                .append("## ~~~\n")
                .append("###### == Basic console commands\n")
                .append("## start - starts the server\n")
                .append("## stop - stops the server\n")
                .append("## restart - restarts the server\n")
                .append("## reset - resets the sequence to 1 (server should be stopped)\n")
                .append("## welcome - prints a welcome message (which you are reading now)\n")
                .append("## help - prints a list of available commands\n")
                .append("## quit - leaves from the application\n")
                .append("###### == Start working with the application, I hope you enjoy == ######");
        System.out.println(welcomeMessage);
    }
}
