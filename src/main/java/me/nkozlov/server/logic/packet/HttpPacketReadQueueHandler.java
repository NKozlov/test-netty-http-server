/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import me.nkozlov.server.logic.AbstractReadQueue;
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
public final class HttpPacketReadQueueHandler extends AbstractReadQueue<HttpRequestSession> {

    private static final Logger logger = LoggerFactory.getLogger(HttpPacketReadQueueHandler.class);

    public HttpPacketReadQueueHandler(int threadPoolSize) {
        super(threadPoolSize);
    }

    @Override
    public void run() {
        logger.debug("{} START.", Thread.currentThread().getName());
        while (!threadPool.isShutdown()) {
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
                //                set http header
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());

                if (!session.isKeepAlive()) {
                    logger.debug("DefaultFullHttpResponse: {}", response);
                    channel.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                    logger.debug("DefaultFullHttpResponse: {}", response);
                    channel.write(response);
                }
                channel.flush();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
