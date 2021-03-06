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
 * Основной контроллер, который запускает и останавливает обработчик очереди {@link HttpSessionReadQueueHandler}.
 * Инициализируется в IoC-контейнере.
 *
 * @author Kozlov Nikita
 * @see HttpSessionReadQueueHandler
 * @see ServerAdminInterface
 */
public class SessionReadQueueAdmin extends AbstractServerAdminInterface implements ServerAdminInterface {

    private static final Logger logger = LoggerFactory.getLogger(SessionReadQueueAdmin.class);

    private HttpSessionReadQueueHandler sessionReadQueueHandler;

    /**
     * Запуск слушателя и обработчика {@link HttpSessionReadQueueHandler} очереди.
     * Два потока.
     */
    @Override
    public void start() {
        if (this.sessionReadQueueHandler == null) {
            //инициализация параметров (из файла config/netty-server-config.properties)
            short httpSessionHandlerCount = Short.parseShort(this.nettyConfig.getProperty("queue.handler.http.session.thread.count"));

            this.sessionReadQueueHandler = new HttpSessionReadQueueHandler(httpSessionHandlerCount);
            //        добавляем в ServerResources ссылку на обработчик очереди
            this.serverResources.setHttpSessionReadQueueHandler(this.sessionReadQueueHandler);
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
        this.serverResources.setHttpSessionReadQueueHandler(null);
    }
}
