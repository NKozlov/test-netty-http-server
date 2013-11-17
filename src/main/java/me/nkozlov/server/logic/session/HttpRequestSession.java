/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic.session;

import me.nkozlov.server.logic.packet.Packet;

/**
 * @author Kozlov Nikita
 */
public class HttpRequestSession extends Session {

    private Packet packet;

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
