/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console.command.impl;

import me.nkozlov.console.command.AbstractCommand;
import me.nkozlov.console.command.RunCommand;

/**
 * Команда, которая осуществляет выход из консоли. Если сервер запущен, то вначале завершает его работу.
 *
 * @author Kozlov Nikita
 * @see RunCommand
 * @see AbstractCommand
 */
public class QuitCommand extends AbstractCommand implements RunCommand {
    /**
     * Метод, который отвечает за запуск команды.
     */
    @Override
    public void apply() {
    }
}
