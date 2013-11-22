/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin;

import me.nkozlov.server.logic.session.HttpSessionReadQueueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * todo Document type SessionReadQueueAdmin
 */
public class SessionReadQueueAdmin extends AbstractServerAdminInterface implements ServerAdminInterface {

    private static final Logger logger = LoggerFactory.getLogger(SessionReadQueueAdmin.class);
    private HttpSessionReadQueueHandler sessionReadQueueHandler;

    /**
     * Запуск слушателя и обработчика {@link HttpSessionReadQueueHandler} очереди.
     * Два потока.
     */
    //    todo добавить возможность конфигурирования кол-ва потоков из *.properties файла.
    @Override
    public void start() {
        if (this.sessionReadQueueHandler == null) {
            this.sessionReadQueueHandler = new HttpSessionReadQueueHandler(2);
            //        добавляем в ServerResources ссылку на обработчик очереди
            this.serverResources.httpSessionReadQueueHandler(this.sessionReadQueueHandler);
        } else {
            logger.info("sessionReadQueueHandler is already running");
        }
    }

    @Override
    public void stop() {
        sessionReadQueueHandler.getThreadPool().shutdown();
        try {
            if (!sessionReadQueueHandler.getThreadPool().awaitTermination(5, TimeUnit.SECONDS)) {
                sessionReadQueueHandler.getThreadPool().shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("{}", e.getMessage());
            throw new RuntimeException(e);
        }

        sessionReadQueueHandler = null;
    }
}
