/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console.command;

/**
 * Интерфейс для команд консоли. Содержит метод {@link me.nkozlov.console.command.RunCommand#apply()}, который запускает выполнение команд.
 * @author Kozlov Nikita
 */
public interface RunCommand {

    /**
     * Метод, который отвечает за запуск команды.
     * */
    void apply();
}
