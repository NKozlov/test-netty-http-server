/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server;

import me.nkozlov.server.logic.file.FileReadQueueHandler;
import me.nkozlov.server.logic.session.HttpSessionReadQueueHandler;

/**
 * Хранит в себе статические ссылки на основные ресурсы, которые использует для своей работы сервера. <br/>
 * <ul>
 * <p/>
 * <li>{@link ServerResources#fileReadQueueHandler} - очередь и ее обработчик задач на запись в файл.</li>
 * <li>{@link ServerResources#httpSessionReadQueueHandler} - очередь и ее обработчик задач на обработку сессий подключений клиентов.</li>
 * </ul>
 * Используется для доступа к ресурсам из любой части кода, посредством статических методов:
 * {@link me.nkozlov.server.ServerResources#getFileReadQueueHandler()} и
 * {@link me.nkozlov.server.ServerResources#getHttpSessionReadQueueHandler()}. <br/>
 * Инициализируется в IoC-контейнере, а значения ссылок инициализируется в соответствующих контроллерах:
 * {@link me.nkozlov.server.admin.FileReadQueueAdmin}, {@link me.nkozlov.server.admin.SessionReadQueueAdmin}
 *
 * @author Kozlov Nikita
 * @see me.nkozlov.server.admin.FileReadQueueAdmin
 * @see me.nkozlov.server.admin.SessionReadQueueAdmin
 */
public class ServerResources {

    private static HttpSessionReadQueueHandler httpSessionReadQueueHandler;
    private static FileReadQueueHandler fileReadQueueHandler;

    public void setHttpSessionReadQueueHandler(HttpSessionReadQueueHandler httpSessionReadQueueHandler) {
        ServerResources.httpSessionReadQueueHandler = httpSessionReadQueueHandler;
    }

    public static HttpSessionReadQueueHandler getHttpSessionReadQueueHandler() {
        return httpSessionReadQueueHandler;
    }

    public static FileReadQueueHandler getFileReadQueueHandler() {
        return fileReadQueueHandler;
    }

    public void setFileReadQueueHandler(FileReadQueueHandler fileReadQueueHandler) {
        ServerResources.fileReadQueueHandler = fileReadQueueHandler;
    }
}
