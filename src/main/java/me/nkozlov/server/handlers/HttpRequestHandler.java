/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
