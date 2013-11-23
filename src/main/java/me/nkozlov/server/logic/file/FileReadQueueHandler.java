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

/**
 * Обработчик очереди задач на запись в файл контента. Запуск и остановка данного обработчика происходит в {@link me.nkozlov.server.admin.FileReadQueueAdmin}.
 * Очередь, которая обрабатывается, находится внутри объекта.
 *
 * @author Kozlov Nikita
 * @see me.nkozlov.server.admin.FileReadQueueAdmin
 * @see AbstractReadQueue
 */
public final class FileReadQueueHandler extends AbstractReadQueue<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(FileReadQueueHandler.class);

    public FileReadQueueHandler(int threadPoolSize) {
        super(threadPoolSize);
    }

    @Override
    public void run() {
        logger.debug("[{}] FileReadQueueHandler.run() START.", Thread.currentThread().getName());

        //        Получаем из IoC-контейнера объект bean name = fileExecutor, для записи в файл
        FileExecutor fileExecutor = ApplicationContextProvider.getApplicationContext().getBean("fileExecutor", FileExecutor.class);

        while (true) {
           /* условия выхода из цикла, логируется в файл.
           проверяется флаг shutdown и обязательно пуста ли очередь с задачами.
           Все задачи, которые хранятся в очереди обязательно должны быть записаны в файл, только после этого можно
           завершить работу.
              */
            if (threadPool.isShutdown() && this.sessionQueue.isEmpty()) {
                logger.info("FileReadQueueHandler can now shutdown properly, because the tasks in the queue anymore.");
                break;
            }
            try {
                Integer content = this.sessionQueue.take();
                logger.trace("FileReadQueueHandler.run() start sessionQueue handle.");

                try {
                    //  thread-safe write
                    fileExecutor.writeToFile(content.toString());
                } catch (IOException e) {
                    logger.error("[{}]: FileReadQueueHandler. e.getMessage() = {}", Thread.currentThread().getName(), e.getMessage());
                }
            } catch (InterruptedException e) {
                logger.info("[{}]: FileReadQueueHandler.", Thread.currentThread().getName());
                if (!this.sessionQueue.isEmpty()) {
                    logger.info("FileReadQueueHandler must complete the processing queue.");
                }
            }
        }
        logger.info("FileReadQueueHandler has successfully completed.");
    }
}
