/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kozlov Nikita
 */
public class HttpRequestHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("http message: {}", msg);

        /*DefaultFullHttpRequest defaultFullHttpRequest = (DefaultFullHttpRequest) msg;

        logger.debug("DefaultFullHttpRequest = {}", defaultFullHttpRequest);*/

    }
}
