/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console.command.impl;

import me.nkozlov.console.command.AbstractCommand;
import me.nkozlov.console.command.RunCommand;

/**
 * Завершает работу сервера.
 *
 * @author Kozlov Nikita
 * @see RunCommand
 * @see AbstractCommand
 */
public class StopCommand extends AbstractCommand implements RunCommand {
    /**
     * Метод, который отвечает за запуск команды.
     */
    @Override
    public void apply() {
    }
}
