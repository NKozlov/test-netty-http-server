/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin;

import me.nkozlov.server.logic.file.FileReadQueueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * Основной контроллер, который запускает и останавливает очередь {@link FileReadQueueHandler}.
 * Инициализируется в IoC-контейнере.
 *
 * @author Kozlov Nikita
 * @see FileReadQueueHandler
 * @see ServerAdminInterface
 */
public class FileReadQueueAdmin extends AbstractServerAdminInterface implements ServerAdminInterface {

    @Autowired
    ServerAdminInterface nettyServerAdmin;

    private static final Logger logger = LoggerFactory.getLogger(FileReadQueueAdmin.class);
    private FileReadQueueHandler fileReadQueueHandler;

    /**
     * Запуск слушателя и обработчика {@link FileReadQueueHandler} очереди.
     * Один поток.
     */
    @Override
    public void start() {
        if (this.fileReadQueueHandler == null) {

            //инициализация параметров (из файла config/netty-server-config.properties)
            short fileQueueHandlerCount = Short.parseShort(this.nettyConfig.getProperty("queue.handler.file.thread.count"));

            this.fileReadQueueHandler = new FileReadQueueHandler(fileQueueHandlerCount);

            //        добавляем в ServerResources ссылку на обработчик очереди
            this.serverResources.setFileReadQueueHandler(this.fileReadQueueHandler);
        } else {
            logger.info("fileReadQueueHandler is already running");
        }
    }

    /**
     * Завершение работы слушателя и обработчика {@link FileReadQueueHandler} очереди.
     */
    @Override
    public void stop() {
        // прерываем выполнение и ставим флаг isShutdown.
        fileReadQueueHandler.getThreadPool().shutdownNow();
        logger.trace("Flag fileReadQueueHandler isShutdown = {}", fileReadQueueHandler.getThreadPool().isShutdown());
        try {
            if (!fileReadQueueHandler.getThreadPool().awaitTermination(10, TimeUnit.SECONDS)) {
                //повторно прерываем для окончательного завершения.
                fileReadQueueHandler.getThreadPool().shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("{}", e.getMessage());
            throw new RuntimeException(e);
        }

        fileReadQueueHandler = null;
        this.serverResources.setFileReadQueueHandler(null);
    }
}
