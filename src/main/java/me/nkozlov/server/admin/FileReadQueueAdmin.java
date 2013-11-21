/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin;

import me.nkozlov.server.logic.file.FileReadQueueHandler;

/**
 * todo Document type FileReadQueueAdmin
 */
public class FileReadQueueAdmin extends AbstractServerAdminInterface implements ServerAdminInterface {

    private FileReadQueueHandler fileReadQueueHandler;

    /**
     * Запуск слушателя и обработчика {@link FileReadQueueHandler} очереди.
     * Один поток.
     */
    //    todo добавить возможность конфигурирования кол-ва потоков из *.properties файла.
    @Override
    public void start() {
        this.fileReadQueueHandler = new FileReadQueueHandler(1);

        //        добавляем в ServerResources ссылку на обработчик очереди
        this.serverResources.setFileReadQueueHandler(this.fileReadQueueHandler);
    }

    @Override
    public void stop() {
    }
}
