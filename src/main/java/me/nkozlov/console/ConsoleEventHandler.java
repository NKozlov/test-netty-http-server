/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.console;

import me.nkozlov.server.admin.ServerAdminInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Обработчик входящих сообщений.
 * Singleton. Инициализируется в IoC-контейнере.
 * <p/>
 * Команды консоли:
 * <ul>
 * <li><u>quit</u> - завершение приложения и выход из консоли</li>
 * <li><u>start</u> - запуск сервера и всех ресурсов, которые он использует</li>
 * <li><u>stop</u> - остановка только сервера. Консоль же остается активной и можно запустить сервер заново.</li>
 * </ul>
 *
 * @author Kozlov Nikita
 * @see me.nkozlov.console.listener.ConsoleEventListener
 * @see ServerAdminInterface
 */
public class ConsoleEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleEventHandler.class);

    @Autowired
    private ServerAdminInterface nettyServerAdmin;

    private static final String COMMAND_QUIT = "quit";
    private static final String COMMAND_START = "start";
    private static final String COMMAND_STOP = "stop";

    /**
     * Обработка входящего сообщения.
     *
     * @param command команда, пришедшая из консоли. Регистр не важен.
     */
    public Boolean processEvent(String command) {
        command = this.parseCommand(command);
        logger.debug("Command input form console: <{}>", command);

        switch (command) {
            case (COMMAND_QUIT):
                return true;
            case (COMMAND_START):
                nettyServerAdmin.start();
                return false;
            case (COMMAND_STOP):
                nettyServerAdmin.stop();
                return false;
            default:
                return false;
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private String parseCommand(String command) {
        return command.trim().toLowerCase();
    }

    // todo Удалить
   /* private void startCommand() {
        Thread threadNetty = new Thread(nettyServer);
        threadNetty.setName("NettyServer Thread");
        this.threadNettyServer = threadNetty;
        threadNetty.start();
    }

    private void stopCommand() {
        //send thread interrupt

        nettyServer.getChannelFuture().channel().close();
        nettyServer.getChannelFuture().awaitUninterruptibly();
    }*/
}
