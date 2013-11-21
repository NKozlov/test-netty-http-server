/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server;

import me.nkozlov.server.logic.file.FileReadQueueHandler;
import me.nkozlov.server.logic.session.HttpSessionReadQueueHandler;

/**
 * @author Kozlov Nikita
 */
public class ServerResources {

    private static HttpSessionReadQueueHandler httpSessionReadQueueHandler;
    private static FileReadQueueHandler fileReadQueueHandler;

    public void httpSessionReadQueueHandler(HttpSessionReadQueueHandler httpSessionReadQueueHandler) {
        ServerResources.httpSessionReadQueueHandler = httpSessionReadQueueHandler;
    }

    public static HttpSessionReadQueueHandler getHttpSessionReadQueueHandler() {
        return httpSessionReadQueueHandler;
    }

    public static FileReadQueueHandler getFileReadQueueHandler() {
        return fileReadQueueHandler;
    }

    public void setFileReadQueueHandler(FileReadQueueHandler fileReadQueueHandler) {
        ServerResources.fileReadQueueHandler = fileReadQueueHandler;
    }
}
