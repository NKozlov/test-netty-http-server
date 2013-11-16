/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console;

import me.nkozlov.server.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Kozlov Nikita
 */
public class ConsoleEventHandler {

    @Autowired
    private NettyServer nettyServer;

    Thread threadNettyServer;

    private static final String COMMAND_QUIT = "quit";
    private static final String COMMAND_START = "start";
    private static final String COMMAND_STOP = "stop";

    private static final Logger logger = LoggerFactory.getLogger(ConsoleEventHandler.class);

    public Boolean processEvent(String command) {
        command = this.parseCommand(command);
        logger.trace("Command input form console: <{}>", command);
        switch (command) {
            case (COMMAND_QUIT):
                return true;
            case (COMMAND_START):
                startCommand();
                return false;
            case (COMMAND_STOP):
                stopCommand();
                return false;
            default:
                return true;
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private String parseCommand(String command) {
        return command.trim().toLowerCase();
    }

    private void startCommand() {
        Thread threadNetty = new Thread(nettyServer);
        threadNetty.setName("NettyServer Thread");
        this.threadNettyServer = threadNetty;
        threadNetty.start();
    }

    private void stopCommand() {
        //send thread interrupt

        nettyServer.getChannelFuture().channel().close();
        nettyServer.getChannelFuture().awaitUninterruptibly();
    }
}
