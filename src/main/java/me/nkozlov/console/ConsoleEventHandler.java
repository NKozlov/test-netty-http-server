/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console;

import me.nkozlov.server.admin.NettyServerAdminInterface;
import me.nkozlov.server.admin.ServerAdminInterface;
import me.nkozlov.server.admin.exceptions.NettyServerAlreadyStartedException;
import me.nkozlov.server.admin.exceptions.NettyServerAlreadyStoppedException;
import me.nkozlov.server.logic.file.FileExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

/**
 * Обработчик входящих сообщений.
 * Singleton. Инициализируется в IoC-контейнере.
 * <p/>
 * Команды консоли:
 * <ul>
 * <li><u>quit</u> - завершение приложения и выход из консоли</li>
 * <li><u>start</u> - запуск сервера и всех ресурсов, которые он использует</li>
 * <li><u>stop</u> - остановка только сервера. Консоль же остается активной и можно запустить сервер заново.</li>
 * </ul>
 *
 * @author Kozlov Nikita
 * @see me.nkozlov.console.listener.ConsoleEventListener
 * @see ServerAdminInterface
 */
public class ConsoleEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleEventHandler.class);

    @Autowired
    @Qualifier(value = "nettyServerAdmin")
    private NettyServerAdminInterface nettyServerAdmin;

    @Autowired
    FileExecutor fileExecutor;

    private static final String COMMAND_QUIT = "quit";
    private static final String COMMAND_START = "start";
    private static final String COMMAND_STOP = "stop";
    private static final String COMMAND_RESTART = "restart";
    private static final String COMMAND_RESET = "reset";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_WELCOME = "welcome";
    private StringBuilder welcomeMessage;

    public ConsoleEventHandler() {
        this.welcomeMessage = new StringBuilder();
        this.welcomeMessage
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
    }

    /**
     * Печатает приветственное сообщение при запуске консоли.
     */
    public void printWelcomeMessage() {
        System.out.println(welcomeMessage);
    }

    /**
     * Обработка входящего сообщения.
     *
     * @param command команда, пришедшая из консоли. Регистр не важен.
     * @return true/false - не продолжаем/продолжаем соответственно слушать консоль.
     */
    public Boolean processEvent(String command) {
        command = this.parseCommand(command);
        logger.debug("Command input form console: <{}>", command);

        // return
        switch (command) {
            case (COMMAND_QUIT):
                this.doCommandQuit();
                return true;
            case (COMMAND_START):
                this.doCommandStart();
                return false;
            case (COMMAND_STOP):
                doCommandStop();
                return false;
            case (COMMAND_RESTART):
                this.doCommandRestart();
                return false;
            case (COMMAND_RESET):
                this.doCommandReset();
                return false;
            case (COMMAND_HELP):
                this.doCommandHelp();
                return false;
            case (COMMAND_WELCOME):
                this.doCommandWelcome();
                return false;
            default:
                doCommandUnknown();
                return false;
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private String parseCommand(String command) {
        return command.trim().toLowerCase();
    }

    private void doCommandStart() {
        try {
            System.out.println("The server is started. Port: 80");
            this.nettyServerAdmin.start();
            System.out.println("The server was started successfully.");
        } catch (NettyServerAlreadyStartedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void doCommandStop() {
        try {
            System.out.println("Server begins its graceful shutdown.");
            this.nettyServerAdmin.stop();
            System.out.println("Server was stopped successfully.");
        } catch (NettyServerAlreadyStoppedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void doCommandRestart() {
        if (this.nettyServerAdmin.checkStatus()) {
            this.doCommandStop();
            this.doCommandStart();
        } else {
            System.out.println("NettyServer is not running, and can not be restarted.");
        }
    }

    private void doCommandReset() {
        if (this.nettyServerAdmin.checkStatus()) {
            System.out.println("Please stop the server before using the reset command.");
        } else {
            try {
                fileExecutor.writeToFile("0");
            } catch (IOException e) {
                logger.error("{}", e.getMessage());
            }
            System.out.println("The sequence is reset to 1.");
        }
    }

    private void doCommandQuit() {
        if (this.nettyServerAdmin.checkStatus()) {
            this.doCommandStop();
        }
        System.out.println("Bye bye, see u :)");
    }

    private void doCommandHelp() {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("###### == Basic console commands\n")
                .append("##\n")
                .append("## start - starts the server\n")
                .append("## stop - stops the server\n")
                .append("## restart - restarts the server\n")
                .append("## reset - resets the sequence to 1 (server should be stopped)\n")
                .append("## welcome - prints a welcome message (which you are reading now)\n")
                .append("## help - prints a list of available commands\n")
                .append("## quit - leaves from the application\n")
                .append("##\n")
                .append("###### == Basic console commands");
        System.out.println(helpMessage);
    }

    private void doCommandWelcome() {
        this.printWelcomeMessage();
    }

    private void doCommandUnknown() {
        System.out.println("Unknown command. To get a list of commands, type \"help\"");
    }
}
