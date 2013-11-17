/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic.packet;

/**
 * @author Kozlov Nikita
 */
public class HttpPacket extends Packet {

    private Object msg;

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
