/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic.file;

import me.nkozlov.server.logic.AbstractReadQueue;
import me.nkozlov.utilz.appcontext.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * @author Kozlov Nikita
 */
public final class FileReadQueueHandler extends AbstractReadQueue<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(FileReadQueueHandler.class);

    public FileReadQueueHandler(int threadPoolSize) {
        super(threadPoolSize);
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    @Override
    public void run() {
        logger.debug("[{}] FileReadQueueHandler.run() START.", Thread.currentThread().getName());
        //        Получаем из IoC-контейнера объект bean name = fileExecutor
        FileExecutor fileExecutor = ApplicationContextProvider.getApplicationContext().getBean("fileExecutor", FileExecutor.class);

        while (!threadPool.isShutdown()) {
            try {
                Integer content = this.sessionQueue.take();
                logger.trace("FileReadQueueHandler.run() start sessionQueue handle.");
                //                this.sessionQueue.isEmpty();
                try {
                    //                    thread-safe write
                    fileExecutor.writeToFile(content.toString());
                } catch (IOException e) {
                    logger.error("[{}]: FileReadQueueHandler. {}", Thread.currentThread().getName(), e.getMessage());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
