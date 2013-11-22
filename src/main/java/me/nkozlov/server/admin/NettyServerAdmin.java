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
 * todo Document type NettyServerAdmin
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
        logger.debug("start()");
        fileReadQueueAdmin.start();
        sessionReadQueueAdmin.start();

        //        Создание отдельного потока для исполнения сервера
        Thread threadNetty = new Thread(nettyServer);
        threadNetty.setName("NettyServer Thread");

        threadNetty.start();
    }

    @Override
    public void stop() {
        nettyServer.getChannelFuture().channel().close();
        nettyServer.getChannelFuture().awaitUninterruptibly();

        sessionReadQueueAdmin.stop();
        fileReadQueueAdmin.stop();
    }
}
