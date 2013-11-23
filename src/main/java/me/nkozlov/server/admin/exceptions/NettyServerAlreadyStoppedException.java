/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin.exceptions;

/**
 * Unchecked исключение, которое сигнализирует о том, что сервер уже остановлен, при попытке остановить не запущенный сервер.
 *
 * @author Kozlov Nikita
 */
public class NettyServerAlreadyStoppedException extends RuntimeException {

    public NettyServerAlreadyStoppedException() {
    }

    public NettyServerAlreadyStoppedException(String message) {
        super(message);
    }

    public NettyServerAlreadyStoppedException(String message, Throwable cause) {
        super(message, cause);
    }
}
