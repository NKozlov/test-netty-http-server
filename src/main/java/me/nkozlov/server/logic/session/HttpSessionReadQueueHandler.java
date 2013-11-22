/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.logic.session;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import me.nkozlov.server.ServerResources;
import me.nkozlov.server.logic.AbstractReadQueue;
import me.nkozlov.server.logic.LogicHandler;
import me.nkozlov.server.logic.NaturalSeqLogicHandler;
import me.nkozlov.utilz.appcontext.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author Kozlov Nikita
 *         todo переместить файл в пакет ession, переименовать класс в HttpRequestSession
 */
public final class HttpSessionReadQueueHandler extends AbstractReadQueue<HttpRequestSession> {

    private static final Logger logger = LoggerFactory.getLogger(HttpSessionReadQueueHandler.class);

    public HttpSessionReadQueueHandler(int threadPoolSize) {
        super(threadPoolSize);
    }

    @Override
    public void run() {
        logger.debug("{} START.", Thread.currentThread().getName());
        LogicHandler logicHandler = ApplicationContextProvider.getApplicationContext().getBean("naturalSeqLogicHandler", NaturalSeqLogicHandler.class);
        while (!threadPool.isShutdown()) {
            try {
                HttpRequestSession session = this.sessionQueue.take();

                Channel channel = session.getChannel();
                //                обрабатываем логику (возвращаем следующее число из последовательности)
                Integer contentValue = (Integer) logicHandler.executeLogic();
                logger.debug("[{}]: contentValue = {}", Thread.currentThread().getName(), contentValue);

                //ставим полученное число в очередь на запись в файл
                try {
                    ServerResources.getFileReadQueueHandler().addTaskToQueue(contentValue);
                } catch (Exception e) {

                    logger.error("Task was not add to FileReadQueueHandler");
                    Thread.sleep(100);
                    DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR,
                            Unpooled.copiedBuffer("500 Internal Server Error", Charset.forName("UTF-8")));
                    response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
                    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());

                    channel.write(response).addListener(ChannelFutureListener.CLOSE);
                    channel.flush();
                    continue;
                }

                //                формируем ответный пакет
                ByteBuf byteBuf = Unpooled.copiedBuffer(contentValue.toString(), Charset.forName("UTF-8"));
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
                logger.info("[{}]: interrupted!", Thread.currentThread().getName());
            }
        }

        logger.info("HttpSessionReadQueueHandler has successfully completed.");
    }
}
