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
import me.nkozlov.server.handlers.HttpRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kozlov Nikita
 */
public class NettyServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private volatile ChannelFuture channelFuture;
    private int port;

    public NettyServer() {
        this.port = 80;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

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
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.

            ChannelFuture f = networkServer.bind(port).sync();
            channelFuture = f;
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.debug("Interrupt detected!!");
        } finally {
            logger.info("Shutdown server.");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
