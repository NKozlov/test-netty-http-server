/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin;

/**
 * Интерфейс, описывающий операции, которые можно производить с сервером и его компонентами.
 *
 * @author Kozlov Nikita
 * @see AbstractServerAdminInterface
 * @see NettyServerAdmin
 * @see FileReadQueueAdmin
 * @see SessionReadQueueAdmin
 */
public interface ServerAdminInterface {

    void start();

    void stop();
}
