/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin;

import me.nkozlov.server.logic.file.FileReadQueueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * todo Document type FileReadQueueAdmin
 */
public class FileReadQueueAdmin extends AbstractServerAdminInterface implements ServerAdminInterface {

    private static final Logger logger = LoggerFactory.getLogger(FileReadQueueAdmin.class);
    private FileReadQueueHandler fileReadQueueHandler;

    /**
     * Запуск слушателя и обработчика {@link FileReadQueueHandler} очереди.
     * Один поток.
     */
    //    todo добавить возможность конфигурирования кол-ва потоков из *.properties файла.
    @Override
    public void start() {
        if (this.fileReadQueueHandler == null) {
            this.fileReadQueueHandler = new FileReadQueueHandler(1);

            //        добавляем в ServerResources ссылку на обработчик очереди
            this.serverResources.setFileReadQueueHandler(this.fileReadQueueHandler);
        } else {
            logger.info("fileReadQueueHandler is already running");
        }
    }

    @Override
    public void stop() {
        fileReadQueueHandler.getThreadPool().shutdown();
        try {
            if (!fileReadQueueHandler.getThreadPool().awaitTermination(5, TimeUnit.SECONDS)) {
                fileReadQueueHandler.getThreadPool().shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("{}", e.getMessage());
            throw new RuntimeException(e);
        }

        fileReadQueueHandler = null;
    }
}
