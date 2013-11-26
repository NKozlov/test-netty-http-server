/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console.command.impl;

import me.nkozlov.console.command.AbstractCommand;
import me.nkozlov.console.command.RunCommand;

/**
 * Производит рестарт сервера, если он запущен.
 *
 * @author Kozlov Nikita
 * @see RunCommand
 * @see AbstractCommand
 */
public class RestartCommand extends AbstractCommand implements RunCommand {
    /**
     * Метод, который отвечает за запуск команды.
     */
    @Override
    public void apply() {
    }
}
