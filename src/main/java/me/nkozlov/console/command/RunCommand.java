/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console.command;

/**
 * Интерфейс для команд консоли. Содержит метод {@link me.nkozlov.console.command.RunCommand#apply()}, который запускает выполнение команд.
 *
 * @author Kozlov Nikita
 * @see me.nkozlov.console.command.impl.HelpCommand
 * @see me.nkozlov.console.command.impl.QuitCommand
 * @see me.nkozlov.console.command.impl.ResetCommand
 * @see me.nkozlov.console.command.impl.RestartCommand
 * @see me.nkozlov.console.command.impl.StartCommand
 * @see me.nkozlov.console.command.impl.StopCommand
 * @see me.nkozlov.console.command.impl.WelcomeCommand
 */
public interface RunCommand {

    /**
     * Метод, который отвечает за запуск команды.
     */
    void apply();

    /**
     * Возвращает имя команды.
     * @return String - имя команды.
     * */
    String getCommandName();
}
