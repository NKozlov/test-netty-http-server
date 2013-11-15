/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kozlov Nikita
 */
public class ConsoleEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleEventHandler.class);

    public Boolean processEvent(String command) {
        command = this.parseCommand(command);
        logger.trace("Command input: <{}>", command);
        return true;
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private String parseCommand(String command) {
        return command.trim().toLowerCase();
    }
}
