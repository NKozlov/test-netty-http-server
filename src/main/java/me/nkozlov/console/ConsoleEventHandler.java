/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console;

import me.nkozlov.console.command.CommandNames;
import me.nkozlov.console.command.RunCommand;
import me.nkozlov.server.admin.ServerAdminInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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

    private static final Map<CommandNames, RunCommand> consoleCommandMap = new HashMap<>();

    public ConsoleEventHandler(RunCommand... runCommands) {
        // наполнение consoleCommandMap
        for (RunCommand runCommand : runCommands) {
            String name = runCommand.getCommandName();
            logger.debug("constructor: commandName = {}", name);
            CommandNames commandName = CommandNames.getFromString(name);
            logger.debug("constructor");
            consoleCommandMap.put(commandName, runCommand);
        }
    }

    /**
     * Печатает приветственное сообщение при запуске консоли.
     */
    public void printWelcomeMessage() {
        //        todo обработать, если такая команда не найдена или map = null
        consoleCommandMap.get(CommandNames.WELCOME).apply();
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
        CommandNames commandName = null;
        try {
            commandName = CommandNames.getFromString(command);
            consoleCommandMap.get(commandName).apply();
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown command, please type help to get a list of available commands.");
        }
        return (commandName != null) && (commandName.equals(CommandNames.QUIT));
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private String parseCommand(String command) {
        return command.trim().toLowerCase();
    }
}
