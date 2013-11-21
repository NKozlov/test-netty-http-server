/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin;

import me.nkozlov.server.logic.session.HttpSessionReadQueueHandler;

/**
 * todo Document type SessionReadQueueAdmin
 */
public class SessionReadQueueAdmin extends AbstractServerAdminInterface implements ServerAdminInterface {

    private HttpSessionReadQueueHandler sessionReadQueueHandler;

    /**
     * Запуск слушателя и обработчика {@link HttpSessionReadQueueHandler} очереди.
     * Два потока.
     */
    //    todo добавить возможность конфигурирования кол-ва потоков из *.properties файла.
    @Override
    public void start() {
        this.sessionReadQueueHandler = new HttpSessionReadQueueHandler(2);
        //        добавляем в ServerResources ссылку на обработчик очереди
        this.serverResources.httpSessionReadQueueHandler(this.sessionReadQueueHandler);
    }

    @Override
    public void stop() {
    }
}
