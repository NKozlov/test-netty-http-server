/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console.command;

/**
 * Перечисление, которое содержит в себе команды консоли.
 *
 * @author Kozlov Nikita
 */
public enum CommandNames {

    START("start"),
    STOP("stop"),
    RESTART("restart"),
    RESET("reset"),
    QUIT("quit"),
    HELP("help"),
    WELCOME("welcome");

    private String commandName;

    private CommandNames(String commandName) {
        this.commandName = commandName;
    }

    /**
     * Ищет константу, которая соответствует тексту.
     *
     * @param commandName - текст команды, которую нужно найти.
     * @return  Возвращает {@link CommandNames}, которая соответствует commandName.
     */
    public static CommandNames getFromString(String commandName) {
        if (commandName != null) {
            for (CommandNames commandNameValue : CommandNames.values()) {
                if (commandName.equalsIgnoreCase(commandNameValue.toString())) {
                    return commandNameValue;
                }
            }
        } else {
            throw new NullPointerException("commandName is null.");
        }
        throw new IllegalArgumentException("No constant with text " + commandName + " found");
    }

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return this.commandName;
    }
}
