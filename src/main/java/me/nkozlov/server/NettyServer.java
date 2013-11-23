/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import me.nkozlov.server.admin.ServerAdminInterface;
import me.nkozlov.server.handlers.HttpRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * Реализация запуска непосредственно netty-сервера, а так же его корректного завершения.
 * Стандартный порт = 80. Сохраняет ссылку на {@link ChannelFuture}, который создается при запуске сервера, для возможности его завершения из вне.
 * Инициализируется в IoC-контейнере.
 *
 * @author Kozlov Nikita
 */
public class NettyServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Autowired
    private ServerAdminInterface nettyServerAdmin;

    private volatile ChannelFuture channelFuture;
    private int port;
    private boolean isStarted;

    public NettyServer() {
        this.port = 80;
        this.isStarted = false;
    }

    /**
     * @return Возвращает ссылку на {@link ChannelFuture} с помощью которого можно будет завершить работу сервера.
     */
    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    /**
     * @return Возвращает порт, который "слушает" сервер. Тип int.
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port номер порта, на котором будет запущен сервер.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return Возвращает статус работы сервера:
     *         <ul>
     *         <li><u>true</u> - запущен. </li>
     *         <li><u>false</u> - не запущен.</li>
     *         </ul>
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * Устанавливает флаг работы сервера.
     *
     * @param started флаг работы сервера:
     *                <ul>
     *                <li><u>true</u> - запущен. </li>
     *                <li><u>false</u> - не запущен.</li>
     *                </ul>
     */
    public void setStarted(boolean started) {
        isStarted = started;
    }

    // synchronized(nettyServerAdmin)
    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap networkServer = new ServerBootstrap();
            networkServer.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpServerCodec(), new HttpObjectAggregator(1048576), new HttpRequestHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 300)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50);

            // Bind and start to accept incoming connections.

            ChannelFuture f = networkServer.bind(port).sync();
            channelFuture = f;
            // возвращаем управление консоли
            synchronized (nettyServerAdmin) {
                nettyServerAdmin.notifyAll();
            }

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.debug("Interrupt detected!!");
        } finally {
            logger.info("Shutdown server.");
            workerGroup.shutdownGracefully().awaitUninterruptibly(10, TimeUnit.SECONDS);
            bossGroup.shutdownGracefully().awaitUninterruptibly(10, TimeUnit.SECONDS);
        }
    }
}
