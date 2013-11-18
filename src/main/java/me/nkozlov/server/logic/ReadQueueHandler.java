/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import me.nkozlov.server.logic.packet.HttpRequestPacket;
import me.nkozlov.server.logic.session.HttpRequestSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Kozlov Nikita
 *         todo надо сделать конструктор без старта потоков, чтобы потоки стартовать только при инициализации сервера
 */
public final class ReadQueueHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ReadQueueHandler.class);

    private final BlockingQueue<HttpRequestSession> sessionQueue;
    private final ExecutorService threadPool;
    private int threadPoolSize;

    public ReadQueueHandler(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        this.sessionQueue = new LinkedBlockingQueue<>();

        initThreadPool();
    }

    // добавление сесси в очередь на обработку
    public void addSessionToProcess(HttpRequestSession session) {
        if (session != null) {
            logger.debug("addSessionToProcess({})", session);
            this.sessionQueue.add(session);
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                HttpRequestSession session = this.sessionQueue.take();

                HttpRequestPacket packet = session.getPacket();

                DefaultFullHttpRequest msg = (DefaultFullHttpRequest) packet.getMsg();
                Channel channel = session.getChannel();

                ByteBuf byteBuf = Unpooled.copiedBuffer("Hello World!", Charset.forName("UTF-8"));
                //                logger debug level
                if (logger.isDebugEnabled()) {
                    String stringByteBuf = "";
                    while (byteBuf.isReadable()) {
                        stringByteBuf = stringByteBuf + (char) byteBuf.readByte();
                    }
                    logger.debug("set content http response ByteBuf = {}", stringByteBuf);
                    byteBuf = byteBuf.resetReaderIndex();
                }

                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

                //                logger debug level
                if (logger.isDebugEnabled()) {
                    String stringByteBuf = "";
                    while (response.content().isReadable()) {
                        stringByteBuf = stringByteBuf + (char) response.content().readByte();
                    }
                    logger.debug("get content http response ByteBuf = {}", stringByteBuf);
                    response.content().resetReaderIndex();
                }
                HttpHeaders httpHeaders = response.headers();
//                 todo задать хедеры
//                response.headers().set(HttpHeaders.Names.CONTENT_TYPE)
                logger.debug("response: header = {}", response.headers());



                logger.debug("DefaultFullHttpResponse: {}", response);
                channel.write(response).addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future) throws Exception {
                        future.channel().close();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void initThreadPool() {
        for (int i = 0; i < this.threadPoolSize; i++) {
            this.threadPool.execute(this);
        }
    }
}
