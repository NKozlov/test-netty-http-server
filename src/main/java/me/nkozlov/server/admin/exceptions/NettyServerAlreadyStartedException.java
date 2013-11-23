/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin.exceptions;

/**
 * Unchecked исключение, которое сигнализирует о том, что сервер уже запущен, при попытке его повторного запуска.
 *
 * @author Kozlov Nikita
 */
public class NettyServerAlreadyStartedException extends RuntimeException {

    public NettyServerAlreadyStartedException() {
    }

    public NettyServerAlreadyStartedException(String message) {
        super(message);
    }

    public NettyServerAlreadyStartedException(String message, Throwable cause) {
        super(message, cause);
    }
}
