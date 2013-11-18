/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server;

import me.nkozlov.server.logic.ReadQueueHandler;

/**
 * @author Kozlov Nikita
 */
public class ServerResources {


    private static ReadQueueHandler readQueueHandler;

    public void setReadQueueHandler(ReadQueueHandler readQueueHandler) {
        ServerResources.readQueueHandler = readQueueHandler;
    }

    public static ReadQueueHandler getReadQueueHandler() {
        return readQueueHandler;
    }
}
