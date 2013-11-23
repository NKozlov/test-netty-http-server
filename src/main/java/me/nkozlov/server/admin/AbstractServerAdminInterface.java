/*
 * Copyright (c) 2013
 * Kozlov Nikita
 */
package me.nkozlov.server.admin;

import me.nkozlov.server.ServerResources;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

/**
 * Общие поля и методы для реализаций интерфейса {@link ServerAdminInterface}.
 *
 * @author Kozlov Nikita
 * @see ServerAdminInterface
 * @see NettyServerAdmin
 * @see FileReadQueueAdmin
 * @see SessionReadQueueAdmin
 */
abstract public class AbstractServerAdminInterface implements ServerAdminInterface {

    @Autowired
    protected ServerResources serverResources;

    @Autowired
    protected Properties nettyConfig;
}
