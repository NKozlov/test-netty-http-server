/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin;

import me.nkozlov.server.NettyServer;
import me.nkozlov.server.admin.exceptions.NettyServerAlreadyStartedException;
import me.nkozlov.server.admin.exceptions.NettyServerAlreadyStoppedException;
import me.nkozlov.server.logic.LogicHandler;
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
 * @see NettyServerAdminInterface
 */
public class NettyServerAdmin extends AbstractServerAdminInterface implements NettyServerAdminInterface {

    @Autowired
    private NettyServer nettyServer;

    @Autowired
    ServerAdminInterface fileReadQueueAdmin;

    @Autowired
    ServerAdminInterface sessionReadQueueAdmin;

    @Autowired
    LogicHandler naturalSeqLogicHandler;

    private static final Logger logger = LoggerFactory.getLogger(NettyServerAdmin.class);

    /**
     * Инициализация сервера и всех ресурсов, необходимых для его работы. При попытке повторного запуска сервера,
     * бросается исключение {@link NettyServerAlreadyStartedException}.<br/>
     * synchronized(this) -- жидает, пока сервер не будет полностью инициализирован.
     *
     * @see me.nkozlov.server.logic.file.FileReadQueueHandler
     * @see me.nkozlov.server.logic.session.HttpSessionReadQueueHandler
     * @see NettyServerAlreadyStartedException
     */
    @Override
    public void start() throws RuntimeException {
        if (!this.checkStatus()) {
            logger.debug("start()");
            fileReadQueueAdmin.start();
            sessionReadQueueAdmin.start();
            naturalSeqLogicHandler.doInit();
            //инициализация параметров для запуска сервера (из файла config/netty-server-config.properties)
            int port = Integer.parseInt(this.nettyConfig.getProperty("netty.server.port"));
            this.nettyServer.setPort(port);

            short bossThreadCount = Short.parseShort(this.nettyConfig.getProperty("netty.server.boss.thread.count"));
            this.nettyServer.setBossThreadCount(bossThreadCount);

            short childThreadCount = Short.parseShort(this.nettyConfig.getProperty("netty.server.child.thread.count"));
            this.nettyServer.setChildThreadCount(childThreadCount);

            //        Создание отдельного потока для исполнения сервера
            Thread threadNetty = new Thread(nettyServer);
            threadNetty.setName("NettyServer Thread");

            threadNetty.start();

            // ждем, пока сервер не инициализируется полностью, только после этого передадим управление консоли
            // перерывается в nettyServer
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    logger.debug("interrupted {}", e.getMessage());
                }
            }
            logger.info("NettyServer was started.");
            //            Устанавливаем состояние серверу "Запущен"
            this.nettyServer.setStarted(true);
        } else {
            // если сервер уже был запущен, то кидаем исключение.
            String message = "NettyServer is already running.";
            logger.info("{}", message);
            throw new NettyServerAlreadyStartedException(message);
        }
    }

    /**
     * Корректное завершение работы сервера и освобождение ресурсов, которые он использует. При попытке
     * остановить не запущенный сервер, бросается исключение {@link NettyServerAlreadyStoppedException}.
     *
     * @see me.nkozlov.server.logic.file.FileReadQueueHandler
     * @see me.nkozlov.server.logic.session.HttpSessionReadQueueHandler
     * @see NettyServerAlreadyStoppedException
     */
    @Override
    public void stop() throws RuntimeException {
        if (this.checkStatus()) {
            nettyServer.getChannelFuture().channel().close();
            nettyServer.getChannelFuture().awaitUninterruptibly();

            sessionReadQueueAdmin.stop();
            logger.debug("sessionReadQueue was stopped.");
            fileReadQueueAdmin.stop();

            //            Устанавливаем состояние серверу "Остановлен"
            this.nettyServer.setStarted(false);
            logger.info("NettyServer was stopped");
        } else {
            String message = "NettyServer is not running, and can not be stopped.";
            logger.info("{}", message);
            throw new NettyServerAlreadyStoppedException(message);
        }
    }

   /**
    * Проверяет статус сервера.
    * @return true - запущен. false - не запущен.
    * */
    @Override
    public boolean checkStatus() {
        return this.nettyServer.isStarted();
    }
}
