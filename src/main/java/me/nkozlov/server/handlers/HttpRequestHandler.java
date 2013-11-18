/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import me.nkozlov.server.ServerResources;
import me.nkozlov.server.logic.packet.HttpRequestPacket;
import me.nkozlov.server.logic.session.HttpRequestSession;
import me.nkozlov.utilz.appcontext.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author Kozlov Nikita
 */
public class HttpRequestHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    /*@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.debug("handlerAdded called! {}", ctx);
    }*/

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelRegistered called! {}; handler = {}", ctx, this);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Context: {}; http message: {}", ctx, msg);

        DefaultFullHttpRequest defaultFullHttpRequest = (DefaultFullHttpRequest) msg;
        HttpRequestPacket packet = new HttpRequestPacket();

        HttpRequestSession session = new HttpRequestSession();
        session.setChannel(ctx.channel());
        session.setPacket(packet);

        logger.debug("readQueueHandler = {}", ServerResources.getReadQueueHandler());
        ServerResources.getReadQueueHandler().addSessionToProcess(session);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelUnregistered called! {}", ctx);
    }

    /*@Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.debug("handlerRemoved called! {}", ctx);
    }*/

   /* @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelInactive called! {}", ctx);
    }*/

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private ApplicationContext getApplicationContext() {
        return ApplicationContextProvider.getApplicationContext();
    }
}
