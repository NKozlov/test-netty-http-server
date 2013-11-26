/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console.command.impl;

import me.nkozlov.console.command.AbstractCommand;
import me.nkozlov.console.command.RunCommand;

/**
 * Запускает сервер.
 *
 * @author Kozlov Nikita
 * @see RunCommand
 * @see AbstractCommand
 */
public class StartCommand extends AbstractCommand implements RunCommand {

    public StartCommand() {
        this.setCommandName("start");
    }

    /**
     * Метод, который отвечает за запуск команды.
     */
    @Override
    public void apply() {
        this.doStartCommand();
    }
}
