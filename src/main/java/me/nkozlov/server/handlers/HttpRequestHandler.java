/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import me.nkozlov.server.ServerResources;
import me.nkozlov.server.logic.packet.HttpRequestPacket;
import me.nkozlov.server.logic.session.HttpRequestSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kozlov Nikita
 */
public class HttpRequestHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelRegistered called! {};", ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Context: {}; http message: {}", ctx, msg);

        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            if (HttpHeaders.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
            }

            if (req.getUri().equalsIgnoreCase("/favicon.ico")) {
                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
            } else {

                boolean keepAlive = HttpHeaders.isKeepAlive(req);
                HttpRequestPacket packet = new HttpRequestPacket();

                HttpRequestSession session = new HttpRequestSession();
                session.setChannel(ctx.channel());
                session.setPacket(packet);
                session.setKeepAlive(keepAlive);

                logger.debug("readQueueHandler = {}", ServerResources.getHttpSessionReadQueueHandler());
                ServerResources.getHttpSessionReadQueueHandler().addTaskToQueue(session);
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
        cause.printStackTrace();
        ctx.close();
    }
}
