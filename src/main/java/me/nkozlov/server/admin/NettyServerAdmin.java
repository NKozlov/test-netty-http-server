/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin;

import me.nkozlov.server.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Основной контроллер, который может корректно запускать и останавливать сервер.
 * Инициализируется в IoC-контейнере.
 *
 * @author Kozlov Nikita
 * @see FileReadQueueAdmin
 * @see SessionReadQueueAdmin
 * @see ServerAdminInterface
 */
public class NettyServerAdmin extends AbstractServerAdminInterface implements ServerAdminInterface {

    @Autowired
    private NettyServer nettyServer;

    @Autowired
    ServerAdminInterface fileReadQueueAdmin;

    @Autowired
    ServerAdminInterface sessionReadQueueAdmin;

    private static final Logger logger = LoggerFactory.getLogger(NettyServerAdmin.class);

    /**
     * Инициализация сервера и всех ресурсов, необходимых для его работы.
     *
     * @see me.nkozlov.server.logic.file.FileReadQueueHandler
     * @see me.nkozlov.server.logic.session.HttpSessionReadQueueHandler
     */
    @Override
    public void start() {
        if (!this.nettyServer.isStarted()) {
            logger.debug("start()");
            fileReadQueueAdmin.start();
            sessionReadQueueAdmin.start();

            //        Создание отдельного потока для исполнения сервера
            Thread threadNetty = new Thread(nettyServer);
            threadNetty.setName("NettyServer Thread");

            threadNetty.start();
            logger.info("NettyServer was started.");
            //            Устанавливаем состояние серверу "Запущен"
            this.nettyServer.setStarted(true);
        } else {
            logger.info("NettyServer is already running.");
        }
    }

    /**
     * Корректное завершение работы сервера и освобождение ресурсов, которые он использует.
     *
     * @see me.nkozlov.server.logic.file.FileReadQueueHandler
     * @see me.nkozlov.server.logic.session.HttpSessionReadQueueHandler
     */
    @Override
    public void stop() {
        if (this.nettyServer.isStarted()) {
            nettyServer.getChannelFuture().channel().close();
            nettyServer.getChannelFuture().awaitUninterruptibly();

            sessionReadQueueAdmin.stop();
            fileReadQueueAdmin.stop();
            //            Устанавливаем состояние серверу "Остановлен"
            this.nettyServer.setStarted(false);
            logger.info("NettyServer was stopped");
        } else {
            logger.info("NettyServer is not running, and can not be stopped.");
        }
    }
}
