/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server;

import me.nkozlov.server.logic.ReadQueueHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Kozlov Nikita
 */
public class ServerResources {

    @Autowired
    private static ReadQueueHandler readQueueHandler;

    public static ReadQueueHandler getReadQueueHandler() {
        return readQueueHandler;
    }
}
