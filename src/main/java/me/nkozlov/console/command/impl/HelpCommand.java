/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console.command.impl;

import me.nkozlov.console.command.AbstractCommand;
import me.nkozlov.console.command.RunCommand;

/**
 * Выводит список доступных команд и их описание.
 *
 * @author Kozlov Nikita
 * @see RunCommand
 * @see AbstractCommand
 */
public class HelpCommand extends AbstractCommand implements RunCommand {
    /**
     * Метод, который отвечает за запуск команды.
     */
    @Override
    public void apply() {
    }
}
