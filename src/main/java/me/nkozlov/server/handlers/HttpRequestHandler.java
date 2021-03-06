/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.handlers;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import me.nkozlov.server.ServerResources;
import me.nkozlov.server.logic.session.HttpRequestSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * Главный обработчик http запросов. Следует в pipeline после {@link HttpServerCodec} и {@link HttpObjectAggregator}.
 *
 * @author Kozlov Nikita
 * @see me.nkozlov.server.NettyServer
 */
public class HttpRequestHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelRegistered called! {};", ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Context: {}; http message: {}", ctx, msg);

        // проверяем, что сообщение действительно HttpRequest
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            if (HttpHeaders.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
            }
            //фильтруем запрос от клиента на /favicon.ico от браузера (тестировалось на Google Chrome)
            if (req.getUri().equalsIgnoreCase("/favicon.ico")) {
                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
                ctx.flush();
            } else {

                boolean keepAlive = HttpHeaders.isKeepAlive(req);
                // создаем объект сессии для текущего подключения и ставим на обработку в очередь.
                HttpRequestSession session = new HttpRequestSession();
                session.setChannel(ctx.channel());
                session.setKeepAlive(keepAlive);

                logger.debug("readQueueHandler = {}", ServerResources.getHttpSessionReadQueueHandler());
                try {
                    ServerResources.getHttpSessionReadQueueHandler().addTaskToQueue(session);
                } catch (Exception e) {
                    logger.error("An internal error occurred while processing the request. {}", e.getStackTrace());
                    throw new Exception(e);
                }
            }
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelUnregistered called! {}", ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR,
                Unpooled.copiedBuffer("500 Internal Server Error", Charset.forName("UTF-8")));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());

        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        ctx.flush();
        logger.error("INTERNAL SERVER ERROR: <{}>", cause.getMessage());
        logger.error("{}", cause.getStackTrace());
        if (logger.isTraceEnabled()) {
            cause.printStackTrace();
        }
        ctx.close();
    }
}
