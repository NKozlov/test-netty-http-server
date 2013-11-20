/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server;

import me.nkozlov.server.logic.packet.HttpPacketReadQueueHandler;

/**
 * @author Kozlov Nikita
 */
public class ServerResources {


    private static HttpPacketReadQueueHandler httpPacketReadQueueHandler;

    public void setReadQueueHandler(HttpPacketReadQueueHandler httpPacketReadQueueHandler) {
        ServerResources.httpPacketReadQueueHandler = httpPacketReadQueueHandler;
    }

    public static HttpPacketReadQueueHandler getHttpPacketReadQueueHandler() {
        return httpPacketReadQueueHandler;
    }
}
