/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin;

/**
 * Интерфейс для {@link me.nkozlov.server.NettyServer}, расширяет возможности {@link ServerAdminInterface}
 *
 * @author Kozlov Nikita
 * @see ServerAdminInterface
 */
public interface NettyServerAdminInterface extends ServerAdminInterface {

    /**
     * Проверяет статус сервера.<br/>
     * <u>true</u> - запущен, <u>false</u> - не запущен.
     */
    boolean checkStatus();
}
