/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic.session;

import io.netty.channel.Channel;
import me.nkozlov.server.logic.packet.HttpRequestPacket;

/**
 * @author Kozlov Nikita
 */
public class HttpRequestSession {

    private HttpRequestPacket packet;
    private Channel channel;

    public HttpRequestPacket getPacket() {
        return packet;
    }

    public void setPacket(HttpRequestPacket packet) {
        this.packet = packet;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
